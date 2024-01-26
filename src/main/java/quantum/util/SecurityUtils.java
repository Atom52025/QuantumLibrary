package quantum.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.experimental.UtilityClass;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@UtilityClass
public class SecurityUtils {

    private static final String SECRET_KEY = "tu-secreto"; // Reemplaza con tu secreto

    public static TokenPayload decodeToken(String token){
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        TokenPayload payload = TokenPayload.fromJsonString( new String(decoder.decode(chunks[1])));
        return payload;
    }

    public void verifyToken(String token) throws Exception {
        String secretKey = "tu_clave_secreta_aqui";

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS256");

        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();
        try {
            jwtParser.parse(token);
        } catch (Exception e) {
            throw new Exception("Could not verify JWT token integrity!", e);
        }
    }

    public static String extractToken(String authorizationHeader) {
        // Extract token from authorization header (format "Bearer <token>")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

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
