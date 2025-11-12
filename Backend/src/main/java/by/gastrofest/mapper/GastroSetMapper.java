package by.gastrofest.mapper;

import by.gastrofest.dbo.GastroSetDbo;
import by.gastrofest.dbo.ParticipantDbo;
import by.gastrofest.dto.GastroSetDto;
import by.gastrofest.parser.model.GastroSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ParticipantMapper.class, GastrofestMapper.class })
public interface GastroSetMapper {

    List<GastroSetDto> toGastroSetDto(final List<GastroSetDbo> gastroSetDbos);

    @Mapping(target = "gastrofest", expression = "java(gastroSetDbo.getGastrofest().getTitle())")
    @Mapping(target = "participant", expression = "java(combineParticipantFields(gastroSetDbo.getParticipant()))")
    @Mapping(target = "restaurant", expression = "java(gastroSetDbo.getParticipant().isRestaurant())")
    GastroSetDto toGastroSetDto(final GastroSetDbo gastroSetDbo);

    GastroSetDbo toGastroSetDbo(final GastroSet gastroSet);

    default String combineParticipantFields(ParticipantDbo participantDbo) {
        return participantDbo.getTitle() + "\n" + participantDbo.getAddress() + "\n" + participantDbo.getPhone();
    }
}
