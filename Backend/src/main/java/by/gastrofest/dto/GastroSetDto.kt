package by.gastrofest.dto

data class GastroSetDto(
    var imageLink: String,
    val url: String,
    val weight: Int,
    val eatOutside: Boolean,
    val bookingPossibility: Boolean,
    val gastrofest: GastrofestDto,
    val participant: String,
    val isRestaurant: Boolean,
)
