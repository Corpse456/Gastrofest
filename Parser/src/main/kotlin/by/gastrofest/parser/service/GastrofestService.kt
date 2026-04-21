package by.gastrofest.parser.service

import by.gastrofest.parser.constant.GASTROFEST_TITLE_CLASS
import by.gastrofest.parser.constant.LOCATION_CLASS
import by.gastrofest.parser.constant.MAIN_POST_CLASS
import by.gastrofest.parser.constant.SRC_PROPERTY
import by.gastrofest.parser.constant.VIDEO_TAG
import by.gastrofest.parser.model.GastroFest
import org.jsoup.nodes.Document
import java.time.LocalDate

fun extractGastrofestFromElement(document: Document): GastroFest {
    val mainElement = document.getElementsByClass(MAIN_POST_CLASS)[0]
    val gastrofestName = mainElement.getElementsByClass(GASTROFEST_TITLE_CLASS)[0].text()
    val imageLink = mainElement.getElementsByTag(VIDEO_TAG)[0].childNodes()[1].absUrl(SRC_PROPERTY)
    val locationTexts = mainElement.getElementsByClass(LOCATION_CLASS)[0].text().split("\\|".toRegex())
    val locations = locationTexts[0].trim()
    val dates = locationTexts[1].trim().split(" - ")
    val startDate = parseDate(dates, 0)
    val endDate = parseDate(dates, 1)
    return GastroFest(null, gastrofestName, imageLink, locations, startDate, endDate)
}

private fun parseDate(dates: List<String>, range: Int): LocalDate {
    return LocalDate.of(
        LocalDate.now().year,
        dates[range].split("\\.".toRegex())[1].toInt(),
        dates[range].split("\\.".toRegex())[0].toInt()
    )
}
