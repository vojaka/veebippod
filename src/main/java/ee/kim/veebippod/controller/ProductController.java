package ee.kim.veebippod.controller;

import ee.kim.veebippod.dto.PageDto;
import ee.kim.veebippod.dto.ProductDto;
import ee.kim.veebippod.entity.Product;
import ee.kim.veebippod.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService productService;

    @GetMapping("allproducts")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    //lisamine
    @PostMapping("products")
    public Product addProduct(@RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    //kustutamine
    @DeleteMapping("products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    //muutmine
    @PutMapping("products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    //yhe vaatamine
    @GetMapping("products/{id}")
    public Product getOneProduct(@PathVariable Long id) throws ExecutionException {
        return productService.getProduct(id);
    }


    @GetMapping("products")
    public Page<Product> getProducts(Pageable pageable, @RequestParam(required = false) Long categoryId) {
        log.info("Fetching products");
        return productService.getProducts(pageable, categoryId);
    }

    @MessageMapping("/products-update") // tellimisel p;;rdume siia
    @SendTo("/get-products") // avalehel subscribe siia toimub
    public Page<Product> getUpdateProducts(@RequestBody PageDto pageDto) {
        return productService.getUpdateProducts(pageDto);
    }
}
