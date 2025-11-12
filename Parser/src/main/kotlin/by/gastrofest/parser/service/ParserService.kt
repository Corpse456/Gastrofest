package by.gastrofest.parser.service

import by.gastrofest.parser.constant.MAIN_PAGE_URL
import by.gastrofest.parser.constant.NODE_RECORD_CLASS
import by.gastrofest.parser.getDocument
import by.gastrofest.parser.model.GastroFest
import by.gastrofest.parser.model.GastroSet
import by.gastrofest.parser.model.Participant
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class ParserService(
    private val gastrofestCommonService: GastrofestCommonService
) {

    fun parseMainPage(): List<GastroSet> {
        val document: Document = getDocument(MAIN_PAGE_URL)
        val gastroFest: GastroFest = extractGastrofestFromElement(document)
        if (!gastrofestCommonService.shouldContinue(gastroFest)) {
            return emptyList()
        }

        val participantsNodes: Elements = document.getElementsByClass(NODE_RECORD_CLASS)
        return participantsNodes.map { parseGastroSet(it, gastroFest) }
    }

    private fun parseGastroSet(participantsNode: Element, savedGastrofest: GastroFest): GastroSet {
        val gastroSet: GastroSet = extractGastroSetInfoFromMainPage(participantsNode)
        val gastroSetDocument: Document = getDocument(gastroSet.url)
        updateGastroSetFromGastroSetPage(gastroSetDocument, gastroSet)

        val participant: Participant = getParticipantFromGastroSetPage(gastroSetDocument)

        gastroSet.gastrofest = savedGastrofest
        gastroSet.participant = participant
        return gastroSet
    }
}
