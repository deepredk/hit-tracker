package it.numble.hittracker.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import it.numble.hittracker.entity.DailyHitLog;
import it.numble.hittracker.entity.Url;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class HitTrackerRepositoryTests {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private DailyHitLogRepository dailyHitLogRepository;

    @Test
    void Url_및_DailyHitLog_신규_저장() {
        Url url = new Url("https://example.com");
        Url savedUrl = urlRepository.save(url);

        LocalDate today = LocalDate.now();
        DailyHitLog dailyHitLog = new DailyHitLog(today, 100, savedUrl);

        dailyHitLogRepository.save(dailyHitLog);

        List<DailyHitLog> dailyHitLogs = dailyHitLogRepository.findAllByUrl(url);
        Url updatedUrl = urlRepository.findById(url.getId()).orElse(null);
        assertThat(updatedUrl).isNotNull();
        assertThat(dailyHitLogs).isNotNull();
        assertThat(dailyHitLogs).hasSize(1);
        assertThat(dailyHitLogs.get(0).getDate()).isEqualTo(today);
        assertThat(dailyHitLogs.get(0).getDailyHit()).isEqualTo(100);
    }
}
