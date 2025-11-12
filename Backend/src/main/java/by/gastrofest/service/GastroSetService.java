package by.gastrofest.service;

import by.gastrofest.dbo.GastroSetDbo;
import by.gastrofest.dto.GastroSetDto;
import by.gastrofest.mapper.GastroSetMapper;
import by.gastrofest.repository.GastroSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GastroSetService {

    private final GastroSetRepository repository;

    private final GastroSetMapper mapper;

    public List<GastroSetDto> findAll() {
        final List<GastroSetDbo> all = repository.findAll();
        return mapper.toGastroSetDto(all);
    }

    public GastroSetDto getById(final Long id) {
        final GastroSetDbo gastroSetDbo = repository.findById(id).orElseThrow();
        return mapper.toGastroSetDto(gastroSetDbo);
    }

    @Transactional
    @SuppressWarnings("UnusedReturnValue")
    public GastroSetDbo save(final GastroSetDbo gastroSetDbo) {
        return repository.findByUrl(gastroSetDbo.getUrl()).orElseGet(() -> repository.save(gastroSetDbo));
    }
}
