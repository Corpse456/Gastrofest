package by.gastrofest.mapper;

import by.gastrofest.dbo.ParticipantDbo;
import by.gastrofest.parser.model.Participant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    ParticipantDbo toParticipantDbo(final Participant participant);
}
