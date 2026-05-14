package ee.kim.veebippod.controller;

import ee.kim.veebippod.dto.ProductDto;
import ee.kim.veebippod.entity.Category;
import ee.kim.veebippod.entity.Product;
import ee.kim.veebippod.repository.CategoryRepository;
import ee.kim.veebippod.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @GetMapping("allproducts")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //lisamine
    @PostMapping("products")
    public Product addProduct(@RequestBody ProductDto productDto) {
        Product product = new Product();
        mapProductDtoToProduct(productDto, product);
        return productRepository.save(product);
    }

    //kustutamine
    @DeleteMapping("products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    //muutmine
    @PutMapping("products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        mapProductDtoToProduct(productDto, existingProduct);
        return productRepository.save(existingProduct);
    }

    //yhe vaatamine
    @GetMapping("products/{id}")
    public Product getOneProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @GetMapping("products")
    public Page<Product> getProducts(Pageable pageable, @RequestParam(required = false) Long categoryId) {
        if (categoryId == null) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findAllByCategoryId(pageable,categoryId);
    }



    private void mapProductDtoToProduct(ProductDto productDto, Product product) {
        product.setName(productDto.name());
        product.setPrice(productDto.price());
        product.setActive(productDto.active());
        product.setStock(productDto.stock());
        product.setDescription(productDto.description());
        product.setImage(productDto.image());

        if (productDto.categoryId() == null) {
            product.setCategory(null);
            return;
        }

        Category category = categoryRepository.findById(productDto.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        product.setCategory(category);
    }

}
