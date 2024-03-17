package by.gastrofest.repository.one;

import by.gastrofest.dbo.one.ParticipantDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantDbo, Long> {

    Optional<ParticipantDbo> findByTitle(String title);
}
