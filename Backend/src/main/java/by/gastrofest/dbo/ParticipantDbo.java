package by.gastrofest.dbo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "participant")
public class ParticipantDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String address;

    private String phone;

    private boolean restaurant;

    @ManyToMany
    @JoinTable(
            name = "participant_working_hours",
            joinColumns = { @JoinColumn(name = "participant_id") },
            inverseJoinColumns = { @JoinColumn(name = "working_hours_id") }
    )
    private Set<WorkingHoursDbo> workingHours;

    @Override
    public String toString() {
        return title;
    }
}
