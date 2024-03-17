package by.gastrofest.mapper;

import by.gastrofest.dbo.one.ParticipantDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    by.gastrofest.dbo.two.ParticipantDbo toTwo(ParticipantDbo one);

    ParticipantDbo toOne(by.gastrofest.dbo.two.ParticipantDbo two);

}
