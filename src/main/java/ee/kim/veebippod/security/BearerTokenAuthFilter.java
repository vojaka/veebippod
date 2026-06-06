package ee.kim.veebippod.security;

import ee.kim.veebippod.entity.Person;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class BearerTokenAuthFilter extends OncePerRequestFilter { // peab olema filter tüüpi
    // valin selle jargi mis autentimist kasutan

    private final JetService jetService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            String accessToken = token.replace("Bearer ", "");
            Person person = jetService.parseToken(accessToken);
            //kuju millisel viisil anname authenticated isiku kaasa ID, logide jaoks ja õigused
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(person.getId(), person.getFirstName()+ " " + person.getLastName(), new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response); //jääb alati lõppu
    }
}
