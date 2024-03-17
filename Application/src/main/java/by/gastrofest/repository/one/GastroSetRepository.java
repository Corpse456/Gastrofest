package by.gastrofest.repository.one;

import by.gastrofest.dbo.GastroSetDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GastroSetRepository extends JpaRepository<GastroSetDbo, Long> {

    Optional<GastroSetDbo> findByUrl(String url);
}
