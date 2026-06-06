package ee.kim.veebippod.service;

import ee.kim.veebippod.dto.PageDto;
import ee.kim.veebippod.dto.ProductDto;
import ee.kim.veebippod.entity.Category;
import ee.kim.veebippod.entity.Product;
import ee.kim.veebippod.repository.CategoryRepository;
import ee.kim.veebippod.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CacheService cacheService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(ProductDto productDto) {
        Product product = new Product();
        product = mapProductDtoToProduct(productDto, product);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        cacheService.deleteProduct(id);
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        mapProductDtoToProduct(productDto, existingProduct);
        cacheService.updateProduct(id);
        return productRepository.save(existingProduct);
    }

    public Product getProduct(Long id) throws ExecutionException {
        try {
            return cacheService.getProduct(id);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof java.util.NoSuchElementException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
            }
            throw e;
        }
    }

    public Page<Product> getProducts(Pageable pageable, Long categoryId) {
        if (categoryId == null) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findAllByCategoryId(pageable, categoryId);
    }

    public Page<Product> getUpdateProducts(PageDto pageDto) {
        return productRepository.findAll(
                PageRequest.of(pageDto.page(), pageDto.size(), Sort.by("stock").ascending())
        );
    }

    public Product mapProductDtoToProduct(ProductDto productDto, Product product) {
        product.setName(productDto.name());
        product.setPrice(productDto.price());
        product.setActive(productDto.active());
        product.setStock(productDto.stock());
        product.setDescription(productDto.description());
        product.setImage(productDto.image());

        if (productDto.categoryId() == null) {
            product.setCategory(null);
            return product;
        }

        Category category = categoryRepository.findById(productDto.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        product.setCategory(category);

        return product;
    }
}
