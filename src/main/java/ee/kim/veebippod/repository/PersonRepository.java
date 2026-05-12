package ee.kim.veebippod.repository;

import ee.kim.veebippod.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);

    boolean existsByEmail(String email);
}
