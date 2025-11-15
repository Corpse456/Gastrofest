package by.gastrofest.controller;

import by.gastrofest.dto.GastroSetDto;
import by.gastrofest.service.GastroSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gastroset")
@RequiredArgsConstructor
public class GastroSetController {

    private final GastroSetService gastroSetService;

    @GetMapping
    public List<GastroSetDto> getAllGastroSets() {
        return gastroSetService.findAll();
    }

    @GetMapping("/{id}")
    public GastroSetDto getGastroSet(@PathVariable("id") Long id) {
        return gastroSetService.getById(id);
    }
}
