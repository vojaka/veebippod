package ee.kim.veebippod.controller;

import ee.kim.veebippod.dto.LoginDto;
import ee.kim.veebippod.dto.SignupDto;
import ee.kim.veebippod.dto.SmartIdDto;
import ee.kim.veebippod.entity.Person;
import ee.kim.veebippod.repository.PersonRepository;
import ee.kim.veebippod.security.JetService;
import ee.kim.veebippod.service.AuthService;
import ee.kim.veebippod.service.SmartIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JetService jetService;
    private final PersonRepository personRepository;
    private final SmartIdService smartIdService;

    @GetMapping("persons")
    public List<Person> getAllPersons() {
        return authService.getAllPersons();
    }

    @PostMapping("login")
    public String login(@RequestBody LoginDto person) {
        if (person.email() == null || person.password() == null) {
            throw new RuntimeException();
        }
        Person dbPerson = personRepository.findByEmail(person.email());
        return jetService.generateToken(dbPerson);
    }

    //signup
    @PostMapping("signup")
    public Person signup(@RequestBody SignupDto signupDto) {
        return authService.signup(signupDto);
    }

    @PostMapping("smart-id")
    public String loginWithSmartId(@RequestBody SmartIdDto smartIdDto) {
        return smartIdService.smartIdLogin(smartIdDto);
    }
//    @PostMapping("smart-id-session")
//    public String startSmartIdSession(@RequestBody DeviceLinkSessionResponse deviceLinkSessionResponse) {
//       return smartIdService.startSmartIdSession(deviceLinkSessionResponse);
//    }
}
