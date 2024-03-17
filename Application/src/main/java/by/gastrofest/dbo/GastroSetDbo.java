package by.gastrofest.dbo;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "gastroset")
@NoArgsConstructor
public class GastroSetDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_base64")
    private String imageBase64;

    @Column(name = "image_link")
    private String imageLink;

    private String url;

    private Integer weight;

    @Column(name = "eat_outside")
    private Boolean eatOutside;

    @Column(name = "booking_possibility")
    private Boolean bookingPossibility;

    @ElementCollection
    @CollectionTable(
            name = "gastroset_meal_image_link",
            joinColumns = @JoinColumn(name = "gastroset_id")
    )
    @Column(name = "image_link")
    private List<String> mealsImages;

    @ElementCollection
    @CollectionTable(
            name = "gastroset_meal_description",
            joinColumns = @JoinColumn(name = "gastroset_id")
    )
    @Column(name = "description")
    private List<String> mealsDescriptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gastrofest_id")
    private GastroFestDbo gastrofest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private ParticipantDbo participant;

    public GastroSetDbo(final String imageBase64, final String imageLink, final String url) {
        this.imageBase64 = imageBase64;
        this.imageLink = imageLink;
        this.url = url;
    }
}
