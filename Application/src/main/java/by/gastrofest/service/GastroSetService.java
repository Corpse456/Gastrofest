package by.gastrofest.service;

import by.gastrofest.dbo.GastroSetDbo;
import by.gastrofest.repository.GastroSetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static by.gastrofest.constant.MainConstants.DATA_THUMB_ATTR;
import static by.gastrofest.constant.MainConstants.HREF_PROPERTY;
import static by.gastrofest.constant.MainConstants.LIST_TAG;
import static by.gastrofest.constant.MainConstants.MANI_PAGE_URL;
import static by.gastrofest.constant.MainConstants.SRC_PROPERTY;
import static by.gastrofest.constant.ParticipantConstants.IMAGES_CLASS;
import static by.gastrofest.constant.ParticipantConstants.IMAGE_CLASS;
import static by.gastrofest.constant.ParticipantConstants.INFO_SUMMARY_CLASS;
import static by.gastrofest.constant.ParticipantConstants.POSITIVE_POSSIBILITY;
import static by.gastrofest.constant.ParticipantConstants.RESERVED_CLASS;
import static by.gastrofest.constant.ParticipantConstants.SET_INFO_CLASS;
import static by.gastrofest.constant.ParticipantConstants.TO_TAKE_CLASS;
import static by.gastrofest.utils.HttpUtil.getEncodedString;

@Service
@RequiredArgsConstructor

public class GastroSetService {

    private final GastroSetRepository repository;

    @Transactional
    @SuppressWarnings("UnusedReturnValue")
    public GastroSetDbo save(final GastroSetDbo gastroSetDbo) {
        return repository.findByUrl(gastroSetDbo.getUrl()).orElseGet(() -> repository.save(gastroSetDbo));
    }

    public GastroSetDbo extractGastroSetInfoFromMainPage(final Element element) {

        final var imageElement = element.getElementsByClass(IMAGE_CLASS).get(0);
        final var imageLink = imageElement.absUrl(SRC_PROPERTY).split("\\?")[0];
        final var imageBase64 = getEncodedString(imageLink);
        final var url = MANI_PAGE_URL + Objects.requireNonNull(imageElement.parent()).attr(HREF_PROPERTY);
        return new GastroSetDbo(imageBase64, imageLink, url);
    }

    public void updateGastroSetFromGastroSetPage(final Document gastroSetDocument, final GastroSetDbo gastroSet) {
        gastroSet.setWeight(executeWeight(gastroSetDocument));
        gastroSet.setMealsDescriptions(executeMealsDescriptions(gastroSetDocument));
        gastroSet.setMealsImages(executeMealsImages(gastroSetDocument));
        gastroSet.setEatOutside(getPossibility(gastroSetDocument, TO_TAKE_CLASS));
        gastroSet.setBookingPossibility(getPossibility(gastroSetDocument, RESERVED_CLASS));

    }

    private static int executeWeight(final Document participantInfoDocument) {
        return Integer.parseInt(
                participantInfoDocument.getElementsByClass(INFO_SUMMARY_CLASS).get(0)
                        .getElementsByClass(SET_INFO_CLASS).get(0)
                        .childNodes().get(1)
                        .childNodes().get(0)
                        .toString()
                        .split(": ")[1]
                        .split(" ")[0]);
    }

    private List<String> executeMealsDescriptions(final Document participantInfoDocument) {
        return participantInfoDocument.getElementsByClass(INFO_SUMMARY_CLASS).get(0)
                .getElementsByTag(LIST_TAG)
                .stream()
                .map(Element::text)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private List<String> executeMealsImages(final Document participantInfoDocument) {
        return participantInfoDocument.getElementsByClass(IMAGES_CLASS).get(0)
                .childNodes()
                .stream()
                .map(node -> node.attr(DATA_THUMB_ATTR))
                .collect(Collectors.toList());
    }

    private static boolean getPossibility(final Document participantInfoDocument, final String className) {
        return POSITIVE_POSSIBILITY.equals(
                participantInfoDocument.getElementsByClass(className).get(0)
                        .getElementsByClass(SET_INFO_CLASS).get(0)
                        .childNodes().get(0)
                        .toString().trim());
    }

}
