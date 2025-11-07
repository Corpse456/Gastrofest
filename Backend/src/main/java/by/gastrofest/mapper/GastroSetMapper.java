package by.gastrofest.mapper;

import by.gastrofest.dbo.GastroFestDbo;
import by.gastrofest.dbo.GastroSetDbo;
import by.gastrofest.dto.GastroSetDto;
import by.gastrofest.dto.GastrofestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GastroSetMapper {

    List<GastroSetDto> toGastroSetDto(final List<GastroSetDbo> gastroSetDbos);

    @Mapping(target = "participant", expression = "java(gastroSetDbo.getParticipant().getTitle())")
    @Mapping(target = "isRestaurant", expression = "java(gastroSetDbo.getParticipant().isRestaurant())")
    GastroSetDto toGastroSetDto(final GastroSetDbo gastroSetDbo);

    GastrofestDto toGastrofestDto(final GastroFestDbo gastrofestDbo);
}
