package ee.kim.veebippod.dto;

import ee.kim.veebippod.entity.Address;
import ee.kim.veebippod.entity.Person;
import ee.kim.veebippod.entity.PersonRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;

import java.io.Serializable;

public record PersonDto(
       Long id,
       String firstName,
       String lastName,
       String email,
       String password,
       String personalCode,
       AddressDto address,
       PersonRole role
) implements Serializable { }
