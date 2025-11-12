package by.gastrofest.dto

data class GastroSetDto(
    var id: Long,
    var imageLink: String,
    val url: String,
    val theme: String,
    val weight: Int,
    val eatOutside: Boolean? = null,
    var booking: Boolean? = null,
    var delivery: Boolean? = null,
    val gastrofest: String,
    val participant: String,
    val restaurant: Boolean? = null,
    var mealsImages: List<String>,
    val mealsDescriptions: List<String>
)
