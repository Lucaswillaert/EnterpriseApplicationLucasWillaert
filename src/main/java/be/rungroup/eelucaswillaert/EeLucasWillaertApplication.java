package be.rungroup.eelucaswillaert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EeLucasWillaertApplication {

    public static void main(String[] args) {
        SpringApplication.run(EeLucasWillaertApplication.class, args);
    }

}
