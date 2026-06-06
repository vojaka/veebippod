package ee.kim.veebippod.security;

import ee.kim.veebippod.entity.Person;
import ee.kim.veebippod.repository.PersonRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JetService {
    private final String superSecretKey = "YXNkZmFzZGZhc2RmZHNhYWRmYXNkZmFzZCBmYWRzZnNhZmpsa2Rhc2pmbGRhc2zDtmYwZXdyK3Fwb2Zrw6RzZGFsZiDDpCfDpGbDtsK0KzE5MzQyODA5MzJ1ODc0aXVoYWxrZGZqw7Zsc2ZkYXNkZg==";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(superSecretKey)) ;
    private final PersonRepository personRepository;

    public JetService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person parseToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long id =  Long.parseLong(claims.getId());
        return  personRepository.findById(id).orElseThrow();
    }

    public String generateToken(Person person){
        Date currentDate  = new Date();
        Date expirationDate  = new Date(currentDate.getTime() + 1000 * 60 * 20); // 20 min
        String token = Jwts.builder()
                .signWith(secretKey)
                .id(person.getId().toString())
                .expiration(expirationDate)
                .compact();
        return token;
    }
}
