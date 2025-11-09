package by.gastrofest.parser.service

import by.gastrofest.parser.constant.MAIN_PAGE_URL
import by.gastrofest.parser.constant.NODE_RECORD_CLASS
import by.gastrofest.parser.getDocument
import by.gastrofest.parser.model.GastroFest
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ParserService(
    val gastrofestCommonService: GastrofestCommonService
) {

    fun parseMainPage() {
        val document: Document = getDocument(MAIN_PAGE_URL)
        val gastroFest: GastroFest = extractGastrofestFromElement(document)
        if (!gastrofestCommonService.shouldContinue(gastroFest)) {
            return
        }

        val participantsNodes: Elements = document.getElementsByClass(NODE_RECORD_CLASS)
        for (participantsNode in participantsNodes) {
            parseGastroSet(participantsNode, gastroFest)
        }
    }

    private fun parseGastroSet(participantsNode: org.jsoup.nodes.Element, savedGastrofest: GastroFest) {
        val gastroSetDbo: GastroSetDbo = gastroSetService.extractGastroSetInfoFromMainPage(participantsNode)
        val gastroSetDocument: Document =
            by.gastrofest.utils.HttpUtil.getDocument(gastroSetDbo.getUrl())
        gastroSetService.updateGastroSetFromGastroSetPage(gastroSetDocument, gastroSetDbo)

        var participant: ParticipantDbo = participantService.getParticipantFromGastroSetPage(gastroSetDocument)
        participant = participantService.save(participant)

        gastroSetDbo.setGastrofest(savedGastrofest)
        gastroSetDbo.setParticipant(participant)
        gastroSetService.save(gastroSetDbo)
    }
}
