package by.gastrofest.service;

import by.gastrofest.dbo.ParticipantDbo;
import by.gastrofest.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final WorkingHoursService workingHoursService;

    private final ParticipantRepository repository;

    @Transactional
    @SuppressWarnings("UnusedReturnValue")
    public ParticipantDbo save(final ParticipantDbo participantDbo) {
        final var workingHoursDbos = participantDbo.getWorkingHours().stream()
                .map(workingHoursService::save)
                .collect(Collectors.toSet());
        participantDbo.setWorkingHours(workingHoursDbos);
        return repository.findByTitleIgnoreCase(participantDbo.getTitle())
                .or(() -> repository.findByAddressIgnoreCaseAndPhone(participantDbo.getAddress(),
                        participantDbo.getPhone()))
                .map(participant -> {
                    if (!workingHoursService.sameWorkingHours(participantDbo, participant)) {
                        participant.setWorkingHours(participantDbo.getWorkingHours());
                    }
                    return participant;
                })
                .orElseGet(() -> repository.save(participantDbo));
    }
}
