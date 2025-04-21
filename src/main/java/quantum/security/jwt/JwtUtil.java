package quantum.security.jwt;

import io.jsonwebtoken.Jwts;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import quantum.exceptions.QuantumLibraryGenericException;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

@UtilityClass
@Slf4j
public class JwtUtil {
    SecretKey key = Jwts.SIG.HS256.key().build();


    /**
     * Get the username from the JWT token.
     */
    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload().getSubject();
    }

    /**
     * Generate a JWT token for the given user.
     */
    public String generateJwtToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .signWith(key)
                .compact();
    }

    /**
     * Verify the integrity of the JWT token.
     */
    public boolean verifyToken(String jwt) throws Exception {
        log.info("Verifying JWT token integrity: {}", jwt);
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
            return true;
        } catch (Exception e) {
            throw new GeneralSecurityException("Could not verify JWT token integrity!", e);
        }
    }

    /**
     * Get the user details from the security context.
     */
    public UserDetails getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        } else {
            throw new IllegalStateException("The object in the security context is not a User.");
        }
    }

    /**
     * Get the user details from the security context.
     */
    public void userIsAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            if (!userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                throw new QuantumLibraryGenericException("Unauthorized", "You don´t have enough permission to make this request", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new IllegalStateException("The object in the security context is not a User.");
        }
    }


}
