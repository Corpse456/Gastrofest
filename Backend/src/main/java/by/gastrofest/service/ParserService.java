package by.gastrofest.service;

import by.gastrofest.dbo.GastroFestDbo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import static by.gastrofest.constant.MainConstants.MAIN_PAGE_URL;
import static by.gastrofest.constant.MainConstants.NODE_RECORD_CLASS;
import static by.gastrofest.utils.HttpUtil.getDocument;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParserService {

    private final GastroSetService gastroSetService;

    private final ParticipantService participantService;

    private final GastrofestService gastrofestService;

    @Transactional
    public void parseMainPage() {
        final var document = getDocument(MAIN_PAGE_URL);
        final var gastrofestDbo = gastrofestService.extractGastrofestFromElement(document);
        if (gastrofestService.exists(gastrofestDbo)) {
            log.info("Gastrofest {} exists", gastrofestDbo.getTitle());
            return;
        }
        final var savedGastrofest = gastrofestService.save(gastrofestDbo);

        final var participantsNodes = document.getElementsByClass(NODE_RECORD_CLASS);
        for (final Element participantsNode : participantsNodes) {
            saveGastroSetDbo(participantsNode, savedGastrofest);
        }
    }

    private void saveGastroSetDbo(final Element participantsNode, final GastroFestDbo savedGastrofest) {
        final var gastroSetDbo = gastroSetService.extractGastroSetInfoFromMainPage(participantsNode);
        final var gastroSetDocument = getDocument(gastroSetDbo.getUrl());
        gastroSetService.updateGastroSetFromGastroSetPage(gastroSetDocument, gastroSetDbo);

        var participant = participantService.getParticipantFromGastroSetPage(gastroSetDocument);
        participant = participantService.save(participant);

        gastroSetDbo.setGastrofest(savedGastrofest);
        gastroSetDbo.setParticipant(participant);
        gastroSetService.save(gastroSetDbo);
    }
}
