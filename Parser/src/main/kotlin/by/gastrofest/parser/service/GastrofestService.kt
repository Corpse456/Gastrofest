package by.gastrofest.parser.service

import by.gastrofest.parser.constant.GASTROFEST_TITLE_CLASS
import by.gastrofest.parser.constant.LOCATION_CLASS
import by.gastrofest.parser.constant.MAIN_POST_CLASS
import by.gastrofest.parser.constant.SOURCE_TAG
import by.gastrofest.parser.constant.SRC_PROPERTY
import by.gastrofest.parser.constant.VIDEO_TAG
import by.gastrofest.parser.model.GastroFest
import org.jsoup.nodes.Document
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.Locale

private val RUSSIAN_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .appendPattern("d MMMM")
    .parseDefaulting(ChronoField.YEAR, Year.now().value.toLong())
    .toFormatter(Locale("ru"))

fun extractGastrofestFromElement(document: Document): GastroFest {
    val mainElement = document.getElementsByClass(MAIN_POST_CLASS)[0]
    val gastrofestName = mainElement.getElementsByClass(GASTROFEST_TITLE_CLASS)[0].text()
    val imageLink = mainElement.getElementsByTag(VIDEO_TAG)[0].getElementsByTag(SOURCE_TAG)[0].absUrl(SRC_PROPERTY)
    val locationTexts = mainElement.getElementsByClass(LOCATION_CLASS)[0].text().split("\\|".toRegex())
    val locations = locationTexts[0].trim()
    val dates = locationTexts[1].trim().split(" - ")
    val startDate = parseDate(dates, 0)
    val endDate = parseDate(dates, 1)
    return GastroFest(null, gastrofestName, imageLink, locations, startDate, endDate)
}

private fun parseDate(dates: List<String>, range: Int): LocalDate =
    LocalDate.parse(dates[range].trim(), RUSSIAN_DATE_FORMATTER)
