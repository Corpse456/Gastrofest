package by.gastrofest.dbo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "gastrofest")
@NoArgsConstructor
public class GastroFestDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String imageBase64;

    private String imageLink;

    private String locations;

    private LocalDate startDate;

    private LocalDate endDate;

    public GastroFestDbo(final String title,
            final String locations,
            final String imageLink,
            final String imageBase64,
            final LocalDate startDate,
            final LocalDate endDate) {
        this.title = title;
        this.locations = locations;
        this.imageLink = imageLink;
        this.imageBase64 = imageBase64;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
