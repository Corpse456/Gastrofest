package by.gastrofest.parser.service

import by.gastrofest.parser.constant.DATA_THUMB_ATTR
import by.gastrofest.parser.constant.HREF_PROPERTY
import by.gastrofest.parser.constant.IMAGES_CLASS
import by.gastrofest.parser.constant.IMAGE_CLASS
import by.gastrofest.parser.constant.INFO_SUMMARY_CLASS
import by.gastrofest.parser.constant.LIST_TAG
import by.gastrofest.parser.constant.MAIN_PAGE_URL
import by.gastrofest.parser.constant.POSITIVE_POSSIBILITY
import by.gastrofest.parser.constant.RESERVED_CLASS
import by.gastrofest.parser.constant.SET_INFO_CLASS
import by.gastrofest.parser.constant.SRC_PROPERTY
import by.gastrofest.parser.constant.TO_TAKE_CLASS
import by.gastrofest.parser.constant.WEIGHT_WORD
import by.gastrofest.parser.model.GastroSet
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import java.util.*
import java.util.stream.Collectors


fun extractGastroSetInfoFromMainPage(element: Element): GastroSet {
    val imageElement = element.getElementsByClass(IMAGE_CLASS)[0]
    val imageLink = imageElement.absUrl(SRC_PROPERTY).split("\\?")[0]
    val url: String = MAIN_PAGE_URL + Objects.requireNonNull<Element?>(imageElement.parent()).attr(HREF_PROPERTY)
    return GastroSet(imageLink = imageLink, url = url)
}

fun updateGastroSetFromGastroSetPage(gastroSetDocument: Document, gastroSet: GastroSet) {
    gastroSet.weight = executeWeight(gastroSetDocument)
    gastroSet.mealsDescriptions = executeMealsDescriptions(gastroSetDocument)
    gastroSet.mealsImages = executeMealsImages(gastroSetDocument)
    gastroSet.eatOutside = getPossibility(gastroSetDocument, TO_TAKE_CLASS)
    gastroSet.bookingPossibility = getPossibility(gastroSetDocument, RESERVED_CLASS)
}

private fun executeWeight(participantInfoDocument: Document): Int? {
    return participantInfoDocument.getElementsByClass(INFO_SUMMARY_CLASS)[0]
        .getElementsByClass(SET_INFO_CLASS)[0]
        .childNodes()
        .stream().filter { node: Node -> node.toString().lowercase(Locale.getDefault()).contains(WEIGHT_WORD) }
        .findAny()
        .map<List<Node>> { obj: Node -> obj.childNodes() }
        .map { weight: List<Node> -> weight[0] }
        .map { obj: Node -> obj.toString() }
        .map { string: String -> string.split(": ")[1] }
        .map { string: String -> string.split(" ")[0] }
        .map { s: String -> s.toInt() }
        .orElse(null)
}

private fun executeMealsDescriptions(participantInfoDocument: Document): List<String> {
    return participantInfoDocument.getElementsByClass(INFO_SUMMARY_CLASS)[0]
        .getElementsByTag(LIST_TAG)
        .stream()
        .map { obj: Element -> obj.text() }
        .map { obj: String -> obj.trim() }
        .collect(Collectors.toList())
}

private fun executeMealsImages(participantInfoDocument: Document): List<String> {
    return participantInfoDocument.getElementsByClass(IMAGES_CLASS)[0]
        .childNodes()
        .stream()
        .map { node: Node -> node.attr(DATA_THUMB_ATTR) }
        .collect(Collectors.toList())
}

private fun getPossibility(participantInfoDocument: Document, className: String): Boolean {
    return POSITIVE_POSSIBILITY == participantInfoDocument.getElementsByClass(className)[0]
        .getElementsByClass(SET_INFO_CLASS)[0]
        .childNodes()[0]
        .toString().trim()
}
