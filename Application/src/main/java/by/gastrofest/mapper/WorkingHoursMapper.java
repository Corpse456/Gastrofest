package by.gastrofest.mapper;

import by.gastrofest.dbo.one.WorkingHoursDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkingHoursMapper {

    by.gastrofest.dbo.two.WorkingHoursDbo toTwo(WorkingHoursDbo one);

    WorkingHoursDbo toOne(by.gastrofest.dbo.two.WorkingHoursDbo two);

}
