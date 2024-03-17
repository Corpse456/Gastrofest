package by.gastrofest.repository.two;

import by.gastrofest.dbo.two.WorkingHoursDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface WorkingHoursRepositoryTwo extends JpaRepository<WorkingHoursDbo, Long> {

    default Optional<WorkingHoursDbo> findSame(WorkingHoursDbo dbo) {
        return findByWeekDaysAndOpenTimeAndCloseTime(dbo.getWeekDays(), dbo.getOpenTime(), dbo.getCloseTime());
    }

    Optional<WorkingHoursDbo> findByWeekDaysAndOpenTimeAndCloseTime(
            String weekDays, LocalTime openTime, LocalTime closeTime);
}
