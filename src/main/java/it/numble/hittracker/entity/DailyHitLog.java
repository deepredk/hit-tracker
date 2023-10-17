package it.numble.hittracker.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DailyHitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private int dailyHit;
    @ManyToOne
    @JoinColumn(name = "url_id", nullable = false)
    private Url url;

    public DailyHitLog(LocalDate date, int dailyHit, Url url) {
        this.date = date;
        this.dailyHit = dailyHit;
        this.url = url;
    }

    public boolean isSevenDaysAgo(LocalDate today) {
        return date.isBefore(today.minusDays(6));
    }
}
