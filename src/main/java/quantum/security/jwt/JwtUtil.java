package quantum.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.util.Base64;

@UtilityClass
@Slf4j
public class JwtUtil {
    SecretKey key = Jwts.SIG.HS256.key().build();

    public static TokenPayload decodeToken(String token) {
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        return TokenPayload.fromJsonString(new String(decoder.decode(chunks[1])));
    }

    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload().getSubject();
    }

    public String generateJwtToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .signWith(key)
                .compact();
    }

    public boolean verifyToken(String jwt) throws Exception {
        log.info("Verifying JWT token integrity: {}", jwt);
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
            return true;
        } catch (Exception e) {
            throw new GeneralSecurityException("Could not verify JWT token integrity!", e);
        }
    }


    @Getter
    public class TokenPayload {
        @JsonProperty("sub")
        private String sub;
        @JsonProperty("name")
        private String name;
        @JsonProperty("iat")
        private long iat; // Puedes cambiar el tipo según tus necesidades

        // Getters y setters

        public static TokenPayload fromJsonString(String jsonString) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonString, TokenPayload.class);
            } catch (Exception e) {
                e.printStackTrace(); // Manejo de errores, podrías lanzar una excepción personalizada aquí
                return null;
            }
        }

        // También puedes agregar métodos específicos según tus necesidades
    }
}
