package ee.kim.veebippod.controller;

import ee.kim.veebippod.dto.ProductDto;
import ee.kim.veebippod.entity.Category;
import ee.kim.veebippod.entity.Product;
import ee.kim.veebippod.repository.CategoryRepository;
import ee.kim.veebippod.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @GetMapping("products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //lisamine
    @PostMapping("products")
    public Product addProduct(@RequestBody ProductDto productDto) {
        Product product = new Product();
        updateProductFields(product, productDto);
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
        Product existingProduct = productRepository.findById(id).orElseThrow();
        updateProductFields(existingProduct, productDto);
        return productRepository.save(existingProduct);
    }

    //yhe vaatamine
    @GetMapping("products/{id}")
    public Product getOneProduct(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    private void updateProductFields(Product product, ProductDto productDto) {
        product.setName(productDto.name());
        product.setPrice(productDto.price());
        product.setActive(productDto.active());
        product.setStock(productDto.stock());
        product.setDescription(productDto.description());
        product.setImage(productDto.image());
        product.setCategory(getCategory(productDto.categoryId()));
    }

    private Category getCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId).orElseThrow();
    }
}
