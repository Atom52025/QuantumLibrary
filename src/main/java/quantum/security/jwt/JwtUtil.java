package quantum.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@UtilityClass
public class JwtUtil {
    private static final String SECRET_KEY = "DAF5B3D55AEC35B676E41B599B31B275237456SDJHFGS32654234FDS";

    public static TokenPayload decodeToken(String token){
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        return TokenPayload.fromJsonString( new String(decoder.decode(chunks[1])));
    }

    public String getUserNameFromJwtToken(String token) {
        return decodeToken(token).getName();
    }

    public boolean verifyToken(String jwt) throws Exception {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt);
            return true;
        } catch (Exception e) {
            throw new Exception("Could not verify JWT token integrity!", e);
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
