package ee.kim.veebippod.repository;

import ee.kim.veebippod.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
Page<Product> findAllByCategoryId(Pageable pageable, Long categoryId);
}
