package by.gastrofest.service;

import by.gastrofest.parser.service.ParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final PersistenceService persistenceService;

    private final ParserService parserService;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void readInfo() {
        final var gastroSets = parserService.parseMainPage();
        persistenceService.saveGastrofestInfos(gastroSets);
        log.info("Update done");
    }

}
