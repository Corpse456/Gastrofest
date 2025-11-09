package by.gastrofest.parser.service

import by.gastrofest.parser.model.GastroFest

interface GastrofestCommonService {

    fun shouldContinue(gastroFest: GastroFest) = true
}
