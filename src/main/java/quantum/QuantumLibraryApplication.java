package quantum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static quantum.util.SecurityUtils.decodeToken;

@Slf4j
@Configuration
@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages = "quantum.model")
public class QuantumLibraryApplication {

	public static void main(String[] args) {

		SpringApplication.run(QuantumLibraryApplication.class, args);

		decodeToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkJhZWxkdW5nIFVzZXIiLCJpYXQiOjE1MTYyMzkwMjJ9.qH7Zj_m3kY69kxhaQXTa-ivIpytKXXjZc1ZSmapZnGE");

		log.info("""
				
				----------------------------------------------
				Application 'Quantum Library' is running!
				Local: 		https://localhost:8080
				----------------------------------------------
				""");
	}

}
