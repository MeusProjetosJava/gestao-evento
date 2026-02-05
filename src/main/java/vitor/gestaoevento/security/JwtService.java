package vitor.gestaoevento.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET =
            "chave-super-secreta-mude-depois-com-32-bytes";

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        var roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())   // Ex: "ROLE_ADMIN"
                .toList();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", roles)  // 👈 claim com authorities
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

}
