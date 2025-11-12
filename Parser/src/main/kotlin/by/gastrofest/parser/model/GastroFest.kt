package by.gastrofest.parser.model

import java.time.LocalDate

data class GastroFest(
    var id: Long? = null,
    val title: String,
    val imageBase64: String,
    val imageLink: String,
    val locations: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)
