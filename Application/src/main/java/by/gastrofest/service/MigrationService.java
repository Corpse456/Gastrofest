package by.gastrofest.service;

import by.gastrofest.dbo.one.GastroSetDbo;
import by.gastrofest.mapper.GastroSetMapper;
import by.gastrofest.mapper.GastrofestMapper;
import by.gastrofest.mapper.ParticipantMapper;
import by.gastrofest.mapper.WorkingHoursMapper;
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

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class MigrationService {

    private final GastrofestRepository gastrofestRepository;

    private final GastrofestRepositoryTwo gastrofestRepositoryTwo;

    private final GastrofestMapper gastrofestMapper;

    private final GastroSetRepository gastroSetRepository;

    private final GastroSetRepositoryTwo gastroSetRepositoryTwo;

    private final GastroSetMapper gastroSetMapper;

    private final ParticipantRepository participantRepository;

    private final ParticipantRepositoryTwo participantRepositoryTwo;

    private final ParticipantMapper participantMapper;

    private final WorkingHoursRepository workingHoursRepository;

    private final WorkingHoursRepositoryTwo workingHoursRepositoryTwo;

    private final WorkingHoursMapper workingHoursMapper;

    @Transactional("transactionManager")
    public void migrateAllCodeBase() {
        final var gastroFestDbos = gastrofestRepositoryTwo.findAll();
        final var oneGastroFests = gastroFestDbos.stream().map(gastrofestMapper::toOne).toList();
        gastrofestRepository.saveAll(oneGastroFests);

        final var workingHoursDbos = workingHoursRepositoryTwo.findAll();
        final var workingHoursOnes = workingHoursDbos.stream().map(workingHoursMapper::toOne).toList();
        workingHoursRepository.saveAll(workingHoursOnes);

        final var participantDbos = participantRepositoryTwo.findAll();
        final var participantOnes = participantDbos.stream().map(participantMapper::toOne).toList();
        participantRepository.saveAll(participantOnes);

        final var gastroSetDbos = gastroSetRepositoryTwo.findAll();
        final var gastroSetOnes = gastroSetDbos.stream()
                .map(gastroSetMapper::toOne)
                .sorted(Comparator.comparing(GastroSetDbo::getId))
                .toList();
        gastroSetRepository.saveAll(gastroSetOnes);
    }
}
