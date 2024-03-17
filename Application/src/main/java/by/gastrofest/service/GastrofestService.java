package by.gastrofest.service;

import by.gastrofest.dbo.one.GastroFestDbo;
import by.gastrofest.repository.one.GastrofestRepository;
import by.gastrofest.repository.two.GastrofestRepositoryTwo;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static by.gastrofest.constant.GastrofestConstants.LOCATION_CLASS;
import static by.gastrofest.constant.GastrofestConstants.MAIN_POST_CLASS;
import static by.gastrofest.constant.GastrofestConstants.TITLE_CLASS;
import static by.gastrofest.constant.MainConstants.IMG_TAG;
import static by.gastrofest.constant.MainConstants.SRC_PROPERTY;
import static by.gastrofest.utils.HttpUtil.getEncodedString;

@Service
@RequiredArgsConstructor

public class GastrofestService {

    private final GastrofestRepository repository;

    private final GastrofestRepositoryTwo repositoryTwo;

    @Transactional
    public GastroFestDbo save(final GastroFestDbo gastrofestDbo) {
        return repository.findByStartDate(gastrofestDbo.getStartDate())
                .orElseGet(() -> repository.save(gastrofestDbo));
    }

    @Transactional("transactionManager")
    public List<GastroFestDbo> getGastrofestDbo1() {
        return repository.findAll();
    }

    @Transactional("transactionManager2")
    public List<by.gastrofest.dbo.two.GastroFestDbo> getGastrofestDbo2() {
        return repositoryTwo.findAll();
    }

    public GastroFestDbo extractGastrofestFromElement(final Document document) {
        final var mainElement = document.getElementsByClass(MAIN_POST_CLASS).get(0);
        final var gastrofestName = mainElement.getElementsByClass(TITLE_CLASS).get(0).text();
        final var imageLink = mainElement.getElementsByTag(IMG_TAG).get(0).absUrl(SRC_PROPERTY);
        final var imageBase64 = getEncodedString(imageLink);
        final var locationText = mainElement.getElementsByClass(LOCATION_CLASS).get(0).text();
        final var locations = locationText.split("\\|")[0].trim();
        final var dates = locationText.split("\\|")[1].trim().split(" - ");
        final var startDate = parseDate(dates, 0);
        final var endDate = parseDate(dates, 1);
        return new GastroFestDbo(gastrofestName, locations, imageLink, imageBase64, startDate, endDate);
    }

    private LocalDate parseDate(final String[] dates, final int range) {
        return LocalDate.of(
                LocalDate.now().getYear(),
                Integer.parseInt(dates[range].split("\\.")[1]),
                Integer.parseInt(dates[range].split("\\.")[0]));
    }
}
