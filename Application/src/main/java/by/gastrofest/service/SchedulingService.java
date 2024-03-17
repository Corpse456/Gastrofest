package by.gastrofest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final ParserService parserService;

    private final GastrofestService gastrofestService;

    @EventListener(ApplicationReadyEvent.class)
    public void readInfo() {
        //        parserService.parseMainPage();
        final var gastrofestDbo2 = gastrofestService.getGastrofestDbo2();
        System.out.println("gastrofestDbo2 = " + gastrofestDbo2);
        log.info("Update done");
    }

}
