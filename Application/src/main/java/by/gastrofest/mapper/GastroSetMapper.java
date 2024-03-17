package by.gastrofest.mapper;

import by.gastrofest.dbo.one.GastroSetDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GastroSetMapper {

    by.gastrofest.dbo.two.GastroSetDbo toTwo(GastroSetDbo one);

    GastroSetDbo toOne(by.gastrofest.dbo.two.GastroSetDbo two);

}
