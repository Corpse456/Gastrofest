package by.gastrofest.parser.model

data class GastroSet(
    var id: Long? = null,
    val imageBase64: String? = null,
    val imageLink: String? = null,
    val url: String? = null,
    val weight: Int? = null,
    val eatOutside: Boolean? = null,
    val bookingPossibility: Boolean? = null,
    val mealsImages: List<String>? = null,
    val mealsDescriptions: List<String>? = null,
    val gastrofest: GastroFest? = null,
    val participant: Participant? = null,
)
