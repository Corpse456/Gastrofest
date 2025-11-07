package by.gastrofest.service;

import by.gastrofest.dbo.GastroFestDbo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import static by.gastrofest.constant.MainConstants.MAIN_PAGE_URL;
import static by.gastrofest.constant.MainConstants.NODE_RECORD_CLASS;
import static by.gastrofest.utils.HttpUtil.getDocument;

@Service
@RequiredArgsConstructor
public class ParserService {

    private final GastroSetService gastroSetService;

    private final ParticipantService participantService;

    private final GastrofestService gastrofestService;

    @Transactional
    public void parseMainPage() {
        final var document = getDocument(MAIN_PAGE_URL);
        final var savedGastrofest = saveGastrofestDbo(document);

        final var participantsNodes = document.getElementsByClass(NODE_RECORD_CLASS);
        for (final Element participantsNode : participantsNodes) {
            saveGastroSetDbo(participantsNode, savedGastrofest);
        }
    }

    private GastroFestDbo saveGastrofestDbo(final Document document) {
        final var gastrofestDbo = gastrofestService.extractGastrofestFromElement(document);
        return gastrofestService.save(gastrofestDbo);
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
