package by.gastrofest.repository.two;

import by.gastrofest.dbo.two.GastroSetDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GastroSetRepositoryTwo extends JpaRepository<GastroSetDbo, Long> {

    Optional<GastroSetDbo> findByUrl(String url);
}
