package by.gastrofest.service;

import by.gastrofest.repository.one.GastroSetRepository;
import by.gastrofest.repository.one.GastrofestRepository;
import by.gastrofest.repository.one.ParticipantRepository;
import by.gastrofest.repository.one.WorkingHoursRepository;
import by.gastrofest.repository.two.GastroSetRepositoryTwo;
import by.gastrofest.repository.two.GastrofestRepositoryTwo;
import by.gastrofest.repository.two.ParticipantRepositoryTwo;
import by.gastrofest.repository.two.WorkingHoursRepositoryTwo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MigrationService {

    private final GastrofestRepository gastrofestRepository;

    private final GastrofestRepositoryTwo gastrofestRepositoryTwo;

    private final GastroSetRepository gastroSetRepository;

    private final GastroSetRepositoryTwo gastroSetRepositoryTwo;

    private final ParticipantRepository participantRepository;

    private final ParticipantRepositoryTwo participantRepositoryTwo;

    private final WorkingHoursRepository workingHoursRepository;

    private final WorkingHoursRepositoryTwo workingHoursRepositoryTwo;

    @Transactional("transactionManager")
    public void migrateAllCodeBase() {
        final var gastroFestDbos = gastrofestRepositoryTwo.findAll();
        gastrofestRepository.saveAll(gastroFestDbos);
        final var workingHoursDbos = workingHoursRepositoryTwo.findAll();
        workingHoursRepository.saveAll(workingHoursDbos);
        final var participantDbos = participantRepositoryTwo.findAll();
        participantRepository.saveAll(participantDbos);
        final var gastroSetDbos = gastroSetRepositoryTwo.findAll();
        gastroSetRepository.saveAll(gastroSetDbos);
    }
}
