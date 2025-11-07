package by.gastrofest.controller

import by.gastrofest.dto.GastroSetDto
import by.gastrofest.service.GastroSetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gastroset")
class GastroSetController(
    private val gastroSetService: GastroSetService
) {

    @GetMapping
    fun getDictionaryData(): MutableList<GastroSetDto>? {
        return gastroSetService.findAll()
    }
}
