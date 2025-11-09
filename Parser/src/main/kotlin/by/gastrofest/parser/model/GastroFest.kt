package by.gastrofest.parser.model

import java.time.LocalDate

data class GastroFest(
    private val id: Long? = null,
    private val title: String? = null,
    private val imageBase64: String? = null,
    private val imageLink: String? = null,
    private val locations: String? = null,
    private val startDate: LocalDate? = null,
    private val endDate: LocalDate? = null,
)
