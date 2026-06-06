package ee.kim.veebippod.configuration;

import ee.kim.veebippod.security.BearerTokenAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


   private final BearerTokenAuthFilter bearerTokenAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(HttpMethod.GET,"/products").permitAll();
                    request.requestMatchers(HttpMethod.GET,"/products/*").permitAll();
                    request.requestMatchers(HttpMethod.GET,"/categories").permitAll();
                    request.requestMatchers(HttpMethod.POST,"/login").permitAll();
                    request.requestMatchers(HttpMethod.POST,"/signup").permitAll();
                    request.requestMatchers(HttpMethod.POST,"/smart-id").permitAll();
                    request.requestMatchers(HttpMethod.POST,"/smart-id-session").permitAll();

                    request.anyRequest().authenticated(); //<-----         SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                })
                .csrf(AbstractHttpConfigurer::disable)
               .addFilterBefore(bearerTokenAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}