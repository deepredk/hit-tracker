package it.numble.hittracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Url {

    @Id
    @Column(name = "url_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @OneToMany
    @JoinColumn(name = "url_id")
    private List<DailyHitLog> dailyHitLogs;

    public Url(String url) {
        this.url = url;
        this.dailyHitLogs = new ArrayList<>();
    }

    public void addLog(DailyHitLog savedDailyHitLog) {
        dailyHitLogs.add(savedDailyHitLog);
    }
}
