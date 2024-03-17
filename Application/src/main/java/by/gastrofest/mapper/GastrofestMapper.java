package by.gastrofest.mapper;

import by.gastrofest.dbo.one.GastroFestDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GastrofestMapper {

    by.gastrofest.dbo.two.GastroFestDbo toTwo(GastroFestDbo one);

    GastroFestDbo toOne(by.gastrofest.dbo.two.GastroFestDbo two);

}
