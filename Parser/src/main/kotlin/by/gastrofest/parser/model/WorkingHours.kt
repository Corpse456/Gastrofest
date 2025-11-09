package by.gastrofest.parser.model

import java.time.LocalTime

data class WorkingHours(
    var id: Long? = null,
    val weekDays: String? = null,
    val openTime: LocalTime? = null,
    val closeTime: LocalTime? = null,
)
