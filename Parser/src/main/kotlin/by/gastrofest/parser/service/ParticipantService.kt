package by.gastrofest.parser.service

import by.gastrofest.parser.constant.DAY_TIME_DELIMETR
import by.gastrofest.parser.constant.DESCRIPTION_CLASS
import by.gastrofest.parser.constant.HREF_PROPERTY
import by.gastrofest.parser.constant.PARTICIPANT_TITLE_CLASS
import by.gastrofest.parser.constant.PHONE_CLASS
import by.gastrofest.parser.constant.REPLACE_WORDS
import by.gastrofest.parser.constant.RESTAURANT_WORD
import by.gastrofest.parser.constant.SET_INFO_CLASS
import by.gastrofest.parser.constant.STREET_CLASS
import by.gastrofest.parser.constant.WORKING_HOURS_CLASS
import by.gastrofest.parser.model.Participant
import by.gastrofest.parser.model.WorkingHours
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.time.LocalTime
import java.util.*
import java.util.regex.Pattern

fun getParticipantFromGastroSetPage(gastroSetDocument: Document): Participant {
    val title = executeTitle(gastroSetDocument)
    val street = gastroSetDocument.getElementsByClass(STREET_CLASS).text()
    val phone = executePhone(gastroSetDocument)
    val description = executeDescription(gastroSetDocument)
    val workingHours: Set<WorkingHours> = executeWorkingHours(gastroSetDocument)
    return Participant(
        title = title,
        address = street,
        phone = phone,
        description = description,
        workingHours = workingHours,
        restaurant = isRestaurant(title)
    )
}

private fun executeTitle(gastroSetDocument: Document): String {
    var titleElementText = gastroSetDocument.getElementsByClass(PARTICIPANT_TITLE_CLASS).text()
    for (word in REPLACE_WORDS) {
        titleElementText = titleElementText.replace(word, "")
    }
    return titleElementText
}

private fun executePhone(participantInfoDocument: Document): String? {
    return if (participantInfoDocument.getElementsByClass(PHONE_CLASS).isEmpty()) {
        null
    } else {
        "+" + participantInfoDocument.getElementsByClass(PHONE_CLASS)[0]
            .getElementsByClass(SET_INFO_CLASS)[0]
            .childNodes()[1]
            .attr(HREF_PROPERTY)
            .split(": ")[1]
            .trim()
            .split("\\+".toRegex())[1]
    }
}

private fun executeDescription(participantInfoDocument: Document): String? {
    return if (participantInfoDocument.hasClass(DESCRIPTION_CLASS)) {
        participantInfoDocument.getElementsByClass(DESCRIPTION_CLASS)[0]
            .getElementsByClass(SET_INFO_CLASS)[0]
            .childNodes()[0]
            .toString()
    } else {
        null
    }
}

private fun executeWorkingHours(participantInfoDocument: Document): Set<WorkingHours> {
    val workingHoursElement = participantInfoDocument.getElementsByClass(WORKING_HOURS_CLASS)
    val workingHoursListStrings = getWorkingHoursList(workingHoursElement)
    val workingHoursList: MutableSet<WorkingHours> = LinkedHashSet<WorkingHours>()

    for (i in workingHoursListStrings.indices) {
        var workingHoursString: String? = workingHoursListStrings[i]
        if (!workingHoursString!!.contains(DAY_TIME_DELIMETR)) {
            workingHoursString = getWorkingHoursString(workingHoursListStrings, i)
            workingHoursString?.let {
                workingHoursListStrings[i] = workingHoursString
            }
        }
        workingHoursList.add(buildWorkingHours(workingHoursString!!))
    }
    return workingHoursList
}

private fun getWorkingHoursString(workingHoursList: List<String>, index: Int): String? {
    var i = index
    val currentString = workingHoursList[i]
    if (!currentString.matches(".*\\d.*".toRegex())) {
        while (i < workingHoursList.size) {
            if (workingHoursList[++i].matches(".*\\d.*".toRegex())) {
                return currentString + DAY_TIME_DELIMETR + workingHoursList[i].split(DAY_TIME_DELIMETR)[0]
            }
        }
        return null
    }
    return workingHoursList[i - 1].split(DAY_TIME_DELIMETR)[0] + DAY_TIME_DELIMETR + currentString
}

private fun getWorkingHoursList(workingHoursElement: Elements): MutableList<String> {
    if (workingHoursElement.isEmpty()) {
        return mutableListOf()
    }

    val rawText = workingHoursElement[0]
        .getElementsByClass(SET_INFO_CLASS)[0]
        .text()

    return getWorkingHoursListWithMatcher(rawText)
}

private fun getWorkingHoursListWithMatcher(rawText: String): MutableList<String> {
    val pattern = Pattern.compile("([а-яА-ЯёЁ\\- ]+): ([\\d: -]+(?:, [\\d: -]+)*)")
    val matcher = pattern.matcher(rawText)

    val result: MutableList<String> = ArrayList()
    while (matcher.find()) {
        val days = matcher.group(1).trim()
        val times = matcher.group(2).trim()
        if (times.contains(",")) {
            val time = times.split(", ")
            for (s in time) {
                result.add("$days: $s")
            }
        } else {
            result.add("$days: $times")
        }
    }
    return result
}

private fun buildWorkingHours(workingHoursString: String): WorkingHours {
    val split: List<String> = workingHoursString.split(DAY_TIME_DELIMETR)
    val weekDays = split[0].trim()
    val times = split[1].split(" - ")
    val startTime = parseTime(times, 0)
    val endTime = parseTime(times, 1)
    return WorkingHours(weekDays = weekDays, openTime = startTime, closeTime = endTime)
}

private fun parseTime(times: List<String>, range: Int): LocalTime {
    return LocalTime.of(
        times[range].split(":")[0].toInt(),
        times[range].split(":")[1].toInt()
    )
}

private fun isRestaurant(title: String): Boolean {
    return title.lowercase(Locale.getDefault()).contains(RESTAURANT_WORD)
}
