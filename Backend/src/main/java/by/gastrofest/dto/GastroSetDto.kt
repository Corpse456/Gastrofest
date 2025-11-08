package by.gastrofest.dto

data class GastroSetDto(
    var id: Long,
    var imageLink: String,
    val url: String,
    val weight: Int,
    val eatOutside: Boolean,
    val bookingPossibility: Boolean,
    val gastrofest: String,
    val participant: String,
    val restaurant: Boolean,
    var mealsImages: List<String>,
    val mealsDescriptions: List<String>
)
