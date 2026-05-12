package ee.kim.veebippod.repository;

import ee.kim.veebippod.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
