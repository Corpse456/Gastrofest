package by.gastrofest.service;

import by.gastrofest.dbo.ParticipantDbo;
import by.gastrofest.dbo.WorkingHoursDbo;
import by.gastrofest.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static by.gastrofest.constant.MainConstants.HREF_PROPERTY;
import static by.gastrofest.constant.ParticipantConstants.DESCRIPTION_CLASS;
import static by.gastrofest.constant.ParticipantConstants.PHONE_CLASS;
import static by.gastrofest.constant.ParticipantConstants.REPLACE_WORDS;
import static by.gastrofest.constant.ParticipantConstants.RESTAURANT_WORD;
import static by.gastrofest.constant.ParticipantConstants.SET_INFO_CLASS;
import static by.gastrofest.constant.ParticipantConstants.STREET_CLASS;
import static by.gastrofest.constant.ParticipantConstants.TITLE_CLASS;
import static by.gastrofest.constant.ParticipantConstants.WORKING_HOURS_CLASS;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    public static final String NOT_WORKING_DAY = "выходной";

    public static final String DAY_TIME_DELIMETR = ": ";

    private final WorkingHoursService workingHoursService;

    private final ParticipantRepository repository;

    @Transactional
    @SuppressWarnings("UnusedReturnValue")
    public ParticipantDbo save(final ParticipantDbo participantDbo) {
        return repository.findByTitleIgnoreCase(participantDbo.getTitle())
                .map(participant -> {
                    if (!workingHoursService.sameWorkingHours(participantDbo, participant)) {
                        participant.setWorkingHours(participantDbo.getWorkingHours());
                    }
                    return participant;
                })
                .orElseGet(() -> repository.save(participantDbo));
    }

    public ParticipantDbo getParticipantFromGastroSetPage(final Document gastroSetDocument) {
        final var title = executeTitle(gastroSetDocument);
        final var street = gastroSetDocument.getElementsByClass(STREET_CLASS).text();
        final var phone = executePhone(gastroSetDocument);
        final var description = executeDescription(gastroSetDocument);
        final var workingHours = executeWorkingHours(gastroSetDocument);
        return new ParticipantDbo(title, street, phone, description, workingHours, isRestaurant(title));
    }

    private static String executeTitle(final Document gastroSetDocument) {
        var titleElementText = gastroSetDocument.getElementsByClass(TITLE_CLASS).text();
        for (String word : REPLACE_WORDS) {
            titleElementText = titleElementText.replaceAll(word, "");
        }
        return titleElementText;
    }

    private static String executePhone(final Document participantInfoDocument) {
        return participantInfoDocument.getElementsByClass(PHONE_CLASS).isEmpty()
                ? null
                : "+" + participantInfoDocument.getElementsByClass(PHONE_CLASS).get(0)
                        .getElementsByClass(SET_INFO_CLASS).get(0)
                        .childNodes().get(1)
                        .attr(HREF_PROPERTY)
                        .split(": ")[1]
                        .trim()
                        .split("\\+")[1];
    }

    private static String executeDescription(final Document participantInfoDocument) {
        return participantInfoDocument.getElementsByClass(DESCRIPTION_CLASS).get(0)
                .getElementsByClass(SET_INFO_CLASS).get(0)
                .childNodes().get(0)
                .toString();
    }

    private Set<WorkingHoursDbo> executeWorkingHours(final Document participantInfoDocument) {
        final var workingHoursElement = participantInfoDocument.getElementsByClass(WORKING_HOURS_CLASS);
        final List<String> workingHoursList = getWorkingHoursList(workingHoursElement);
        final Set<WorkingHoursDbo> workingHoursDboList = new LinkedHashSet<>();

        for (int i = 0; i < workingHoursList.size(); i++) {
            String workingHoursString = workingHoursList.get(i);
            if (!workingHoursString.contains(DAY_TIME_DELIMETR)) {
                workingHoursString = getWorkingHoursString(workingHoursList, i);
                if (workingHoursString != null) {
                    workingHoursList.set(i, workingHoursString);
                }
            }
            workingHoursDboList.add(buildWorkingHours(workingHoursString));
        }
        return workingHoursDboList;
    }

    private static String getWorkingHoursString(final List<String> workingHoursList, int i) {
        final String currentString = workingHoursList.get(i);
        if (!currentString.matches(".*\\d.*")) {
            while (i < workingHoursList.size()) {
                if (workingHoursList.get(++i).matches(".*\\d.*")) {
                    return currentString + DAY_TIME_DELIMETR + workingHoursList.get(i).split(DAY_TIME_DELIMETR)[1];
                }
            }
            return null;
        }
        return workingHoursList.get(i - 1).split(DAY_TIME_DELIMETR)[0] + DAY_TIME_DELIMETR + currentString;
    }

    private static List<String> getWorkingHoursList(final Elements workingHoursElement) {
        return Arrays.stream(workingHoursElement.isEmpty()
                        ? new String[0]
                        : workingHoursElement.get(0)
                                .getElementsByClass(SET_INFO_CLASS).get(0)
                                .text().split(", "))
                .filter(workingHoursString -> !workingHoursString.contains(NOT_WORKING_DAY))
                .collect(Collectors.toList());
    }

    private WorkingHoursDbo buildWorkingHours(final String workingHoursString) {
        final var split = workingHoursString.split(DAY_TIME_DELIMETR);
        final var weekDays = split[0].trim();
        final var times = split[1].split(" - ");
        final var startTime = parseTime(times, 0);
        final var endTime = parseTime(times, 1);
        return workingHoursService.save(new WorkingHoursDbo(weekDays, startTime, endTime));
    }

    private LocalTime parseTime(final String[] times, final int range) {
        return LocalTime.of(
                Integer.parseInt(times[range].split(":")[0]),
                Integer.parseInt(times[range].split(":")[1]));
    }

    private static boolean isRestaurant(final String title) {
        return title.toLowerCase().contains(RESTAURANT_WORD);
    }
}
