package by.gastrofest.service;

import by.gastrofest.mapper.GastroSetMapper;
import by.gastrofest.parser.model.GastroSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersistenceService {

    private final ParticipantService participantService;

    private final GastroSetService gastroSetService;

    private final GastroSetMapper gastroSetMapper;

    @Transactional
    public void saveGastrofestInfos(final List<GastroSet> gastroSets) {
        for (final GastroSet gastroSet : gastroSets) {
            final var gastroSetDbo = gastroSetMapper.toGastroSetDbo(gastroSet);

            final var participantDbo = participantService.save(gastroSetDbo.getParticipant());
            gastroSetDbo.setParticipant(participantDbo);
            gastroSetService.save(gastroSetDbo);
        }
    }
}
