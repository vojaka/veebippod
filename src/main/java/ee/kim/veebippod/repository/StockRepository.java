package ee.kim.veebippod.repository;


import ee.kim.veebippod.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {}
