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

    @EventListener(ApplicationReadyEvent.class)
    public void readInfo() {
        parserService.parseMainPage();
        log.info("Update done");
    }

}
