package quantum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages = "quantum.model")
public class QuantumLibraryApplication {

    public static void main(String[] args) {

        SpringApplication.run(QuantumLibraryApplication.class, args);

        log.info("""
                				
                ----------------------------------------------
                Application 'Quantum Library' is running!
                ----------------------------------------------
                """);
    }

}
