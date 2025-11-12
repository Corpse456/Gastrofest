package by.gastrofest.parser.model

data class GastroSet(
    var id: Long? = null,
    var imageLink: String? = null,
    val url: String,
    var weight: Int? = null,
    var eatOutside: Boolean? = null,
    var bookingPossibility: Boolean? = null,
    var mealsImages: List<String>? = null,
    var mealsDescriptions: List<String>? = null,
    var gastrofest: GastroFest? = null,
    var participant: Participant? = null,
)
