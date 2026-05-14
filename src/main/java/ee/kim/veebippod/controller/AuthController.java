package ee.kim.veebippod.controller;

import ee.kim.veebippod.dto.LoginDto;
import ee.kim.veebippod.dto.SignupDto;
import ee.kim.veebippod.entity.Person;
import ee.kim.veebippod.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final PersonRepository personRepository;

    @GetMapping("persons")
    public List<Person> getallpersons()
    {
        return personRepository.findAll();
    }

    @PostMapping("login")
    public String login(@RequestBody LoginDto person) {
        if (person.email()== null || person.password() == null) {
            throw new RuntimeException();
        }
        return "Edukalt sisse logitud!";
    }



    //signup
    @PostMapping("signup")
    public Person signup(@RequestBody SignupDto signupDto) {
        if (signupDto.email() == null || signupDto.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        if (personRepository.existsByEmail(signupDto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }

        Person person = new Person();
        person.setFirstName(signupDto.firstName());
        person.setLastName(signupDto.lastName());
        person.setEmail(signupDto.email());
        person.setPassword(signupDto.password());
        person.setPersonalCode(signupDto.personalCode());

        return personRepository.save(person);
    }

}
