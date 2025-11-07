package by.gastrofest.dbo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "working_hours")
@EqualsAndHashCode
public class WorkingHoursDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    private String weekDays;

    private LocalTime openTime;

    private LocalTime closeTime;

    public WorkingHoursDbo(final String weekDays, final LocalTime openTime, final LocalTime closeTime) {
        this.weekDays = weekDays;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

}
