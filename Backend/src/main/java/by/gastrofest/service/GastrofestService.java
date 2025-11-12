package by.gastrofest.service;

import by.gastrofest.dbo.GastroFestDbo;
import by.gastrofest.repository.GastrofestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GastrofestService {

    private final GastrofestRepository repository;

    public boolean existsByStartDate(final LocalDate startDate) {
        return repository.findByStartDate(startDate).isPresent();
    }

    @Transactional
    public GastroFestDbo save(final GastroFestDbo gastrofestDbo) {
        return repository.save(gastrofestDbo);
    }
}
