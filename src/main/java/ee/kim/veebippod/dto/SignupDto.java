package ee.kim.veebippod.dto;

public record SignupDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String personalCode
) {
}
