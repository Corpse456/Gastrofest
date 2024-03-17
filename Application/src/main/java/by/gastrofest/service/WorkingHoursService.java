package by.gastrofest.service;

import by.gastrofest.dbo.one.ParticipantDbo;
import by.gastrofest.dbo.one.WorkingHoursDbo;
import by.gastrofest.repository.one.WorkingHoursRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class WorkingHoursService {

    private final WorkingHoursRepository repository;

    @Transactional
    public WorkingHoursDbo save(final WorkingHoursDbo workingHoursDbo) {
        return repository.findSame(workingHoursDbo).orElseGet(() -> repository.save(workingHoursDbo));
    }

    public boolean sameWorkingHours(final ParticipantDbo participantDbo, final ParticipantDbo participant) {
        return participant.getWorkingHours().size() == participantDbo.getWorkingHours().size()
               && participant.getWorkingHours().containsAll(participantDbo.getWorkingHours());
    }
}
