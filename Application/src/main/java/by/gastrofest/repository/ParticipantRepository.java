package by.gastrofest.repository;

import by.gastrofest.dbo.ParticipantDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantDbo, Long> {

    Optional<ParticipantDbo> findByTitleIgnoreCase(String title);
}
