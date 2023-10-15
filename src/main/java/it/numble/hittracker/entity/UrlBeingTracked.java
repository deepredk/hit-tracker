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
public class UrlBeingTracked {

    @Id
    @Column(name = "url_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private int dailyHit;
    private int totalHit;
    @OneToMany
    @JoinColumn(name = "url_id")
    private List<DailyHitLog> dailyHitLogs;

    public UrlBeingTracked(String url) {
        this.url = url;
        this.dailyHit = 0;
        this.totalHit = 0;
        this.dailyHitLogs = new ArrayList<>();
    }

    public void hit() {
        this.dailyHit += 1;
        this.totalHit += 1;
    }

    public void initiailizeDailyHit() {
        this.dailyHit = 0;
    }

    public void addLog(DailyHitLog savedDailyHitLog) {
        dailyHitLogs.add(savedDailyHitLog);
    }
}
