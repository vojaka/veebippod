package ee.kim.veebippod.repository;

import ee.kim.veebippod.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
