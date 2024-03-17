package by.gastrofest.repository.two;

import by.gastrofest.dbo.two.ParticipantDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepositoryTwo extends JpaRepository<ParticipantDbo, Long> {

    Optional<ParticipantDbo> findByTitle(String title);
}
