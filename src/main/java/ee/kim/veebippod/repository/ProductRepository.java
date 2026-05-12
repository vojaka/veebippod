package ee.kim.veebippod.repository;

import ee.kim.veebippod.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
