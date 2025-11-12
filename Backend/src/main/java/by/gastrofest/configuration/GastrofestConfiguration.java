package by.gastrofest.configuration;

import by.gastrofest.parser.service.GastrofestCommonService;
import by.gastrofest.parser.service.ParserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GastrofestConfiguration {

    @Bean
    public ParserService parserService(GastrofestCommonService gastrofestCommonService) {
        return new ParserService(gastrofestCommonService);
    }
}
