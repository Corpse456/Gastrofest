package by.gastrofest.parser.model

data class Participant(
    var id: Long? = null,
    val title: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val description: String? = null,
    val restaurant: Boolean = false,
    val workingHours: Set<WorkingHours>? = null
)
