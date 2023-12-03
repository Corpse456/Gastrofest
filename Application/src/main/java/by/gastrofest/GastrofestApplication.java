package by.gastrofest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class GastrofestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GastrofestApplication.class)
                .headless(false)
                .run(args);
        System.exit(0);
    }

}
