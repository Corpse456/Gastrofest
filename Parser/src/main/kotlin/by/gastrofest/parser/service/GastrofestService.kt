package by.gastrofest.parser.service

import by.gastrofest.parser.constant.GASTROFEST_TITLE_CLASS
import by.gastrofest.parser.constant.IMG_TAG
import by.gastrofest.parser.constant.LOCATION_CLASS
import by.gastrofest.parser.constant.MAIN_POST_CLASS
import by.gastrofest.parser.constant.SRC_PROPERTY
import by.gastrofest.parser.getEncodedString
import by.gastrofest.parser.model.GastroFest
import org.jsoup.nodes.Document
import java.time.LocalDate

fun extractGastrofestFromElement(document: Document): GastroFest {
    val mainElement = document.getElementsByClass(MAIN_POST_CLASS)[0]
    val gastrofestName = mainElement.getElementsByClass(GASTROFEST_TITLE_CLASS)[0].text()
    val imageLink = mainElement.getElementsByTag(IMG_TAG)[0].absUrl(SRC_PROPERTY)
    val imageBase64: String = getEncodedString(imageLink)
    val locationText = mainElement.getElementsByClass(LOCATION_CLASS)[0].text()
    val locations =
        locationText.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }
    val dates =
        locationText.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }
            .split(" - ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val startDate = parseDate(dates, 0)
    val endDate = parseDate(dates, 1)
    return GastroFest(null, gastrofestName, locations, imageLink, imageBase64, startDate, endDate)
}

private fun parseDate(dates: Array<String>, range: Int): LocalDate {
    return LocalDate.of(
        LocalDate.now().year,
        dates[range].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt(),
        dates[range].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
    )
}
