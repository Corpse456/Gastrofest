package by.gastrofest.service;

import by.gastrofest.dbo.ParticipantDbo;
import by.gastrofest.repository.ParticipantRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
                .or(() -> getByAddressAndPhone(participantDbo))
                .map(participant -> mapWorkingHours(participantDbo, participant))
                .orElseGet(() -> repository.save(participantDbo));
    }

    private Optional<ParticipantDbo> getByAddressAndPhone(final ParticipantDbo participantDbo) {
        if (StringUtils.isEmpty(participantDbo.getAddress()) && StringUtils.isEmpty(participantDbo.getPhone())) {
            return Optional.empty();
        }
        return repository.findByAddressIgnoreCaseAndPhone(participantDbo.getAddress(), participantDbo.getPhone());
    }

    private ParticipantDbo mapWorkingHours(final ParticipantDbo participantDbo, final ParticipantDbo participant) {
        if (!workingHoursService.sameWorkingHours(participantDbo, participant)) {
            participant.setWorkingHours(participantDbo.getWorkingHours());
        }
        return participant;
    }
}
