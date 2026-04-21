package by.gastrofest.parser.service

import by.gastrofest.parser.constant.NODE_RECORD_CLASS
import by.gastrofest.parser.constant.SCRAPE_URL
import by.gastrofest.parser.getDocument
import by.gastrofest.parser.model.GastroFest
import by.gastrofest.parser.model.GastroSet
import by.gastrofest.parser.model.Participant
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class ParserService(
    private val gastrofestCommonService: GastrofestCommonService
) {

    fun getGastroSetsList3(): List<GastroSet> {
        val objectMapper = jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
        val inputStream = Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream("10-th.Gastrofest.json")
            ?: error("File not found in resources")

        val json = inputStream.bufferedReader().readText()
        return objectMapper.readValue(json)
    }

    fun getGastroSetsList(): List<GastroSet> {
        val document: Document = getDocument(SCRAPE_URL)
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
