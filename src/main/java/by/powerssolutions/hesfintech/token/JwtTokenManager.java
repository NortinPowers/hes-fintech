package by.powerssolutions.hesfintech.token;

import static by.powerssolutions.hesfintech.utils.Constants.ROLES;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager {

    private final SecretKey key;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    /**
     * Создает экземпляр менеджера JWT-токенов с указанным секретом.
     *
     * @param secret Секрет для создания ключа.
     */
    public JwtTokenManager(@Value("${jwt.secret}") String secret) {
        this.key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HS256.getJcaName());
    }

    /**
     * Генерирует JWT-токен на основе данных о пользователе.
     *
     * @param userDetails Данные о пользователе.
     * @return Сгенерированный JWT-токен.
     */
    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put(ROLES, roles);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .expiration(expiredDate)
                .signWith(key)
                .compact();
    }

    /**
     * Получает имя пользователя из JWT-токена.
     *
     * @param token JWT-токен.
     * @return Имя пользователя.
     */
    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Получает роли пользователя из JWT-токена.
     *
     * @param token JWT-токен.
     * @return Список ролей пользователя.
     */
    @SuppressWarnings("unchecked")
    public List<String> getUserRoles(String token) {
        return getAllClaimsFromToken(token).get(ROLES, List.class);
    }

    /**
     * Получает все утверждения (claims) из JWT-токена.
     *
     * @param token JWT-токен.
     * @return Объект Claims, содержащий информацию из токена.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
