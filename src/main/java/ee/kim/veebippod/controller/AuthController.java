package ee.kim.veebippod.controller;

import ee.kim.veebippod.dto.AuthResponseDto;
import ee.kim.veebippod.dto.LoginDto;
import ee.kim.veebippod.dto.SignupDto;
import ee.kim.veebippod.entity.Person;
import ee.kim.veebippod.repository.PersonRepository;
import ee.kim.veebippod.service.JwtService;
import ee.kim.veebippod.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final PersonRepository personRepository;
    private final JwtService jwtService;
    private final PasswordService passwordService;

    @GetMapping("persons")
    public List<Person> getallpersons()
    {
        return personRepository.findAll();
    }

    @PostMapping("login")
    public AuthResponseDto login(@RequestBody LoginDto loginDto) {
        validateLoginDto(loginDto.email(), loginDto.password());

        Person person = personRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordService.matches(loginDto.password(), person.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return new AuthResponseDto(jwtService.generateToken(person));
    }



    //signup
    @PostMapping("signup")
    public AuthResponseDto signup(@RequestBody SignupDto signupDto) {
        validateLoginDto(signupDto.email(), signupDto.password());

        if (personRepository.existsByEmail(signupDto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }

        Person person = new Person();
        person.setFirstName(signupDto.firstName());
        person.setLastName(signupDto.lastName());
        person.setEmail(signupDto.email());
        person.setPassword(passwordService.hash(signupDto.password()));
        person.setPersonalCode(signupDto.personalCode());

        Person savedPerson = personRepository.save(person);

        return new AuthResponseDto(jwtService.generateToken(savedPerson));
    }

    private void validateLoginDto(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }
    }

}
