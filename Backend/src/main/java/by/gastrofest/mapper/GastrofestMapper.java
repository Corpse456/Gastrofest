package by.gastrofest.mapper;

import by.gastrofest.dbo.GastroFestDbo;
import by.gastrofest.parser.model.GastroFest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GastrofestMapper {

    GastroFestDbo toGastroFestDbo(final GastroFest gastrofest);
}
