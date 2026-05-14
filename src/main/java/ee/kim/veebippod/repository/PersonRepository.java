package ee.kim.veebippod.repository;

import ee.kim.veebippod.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByEmail(String email);
}
