package by.gastrofest.repository.one;

import by.gastrofest.dbo.one.GastroFestDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GastrofestRepository extends JpaRepository<GastroFestDbo, Long> {

    Optional<GastroFestDbo> findByStartDate(LocalDate startDate);
}
