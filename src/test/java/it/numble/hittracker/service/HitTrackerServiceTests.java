package it.numble.hittracker.service;

import it.numble.hittracker.controller.dto.UrlHitInfoDto;
import it.numble.hittracker.entity.DailyHitLog;
import it.numble.hittracker.entity.Url;
import it.numble.hittracker.repository.DailyHitLogRepository;
import it.numble.hittracker.repository.HitRepository;
import it.numble.hittracker.repository.IsCreatedUrlRepository;
import it.numble.hittracker.repository.UrlRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestRedisConfiguration.class)
@Transactional
public class HitTrackerServiceTests {

    @Autowired
    private HitTrackerService hitTrackerService;

    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private DailyHitLogRepository dailyHitLogRepository;
    @Autowired
    private HitRepository hitRepository;
    
    @BeforeEach
    void setUp() {
        hitRepository.flushAll();
        urlRepository.deleteAll();
        dailyHitLogRepository.deleteAll();
    }

    @Test
    void getHitInfo() {
        // given
        String url = "http://test.com";
        hitRepository.hit(url);

        // when
        UrlHitInfoDto hitInfo = hitTrackerService.getHitInfo(url);

        // then
        assertThat(hitInfo.getTodayHit()).isEqualTo(1);
        assertThat(hitInfo.getTotalHit()).isEqualTo(1);
    }

    @Test
    void hit() throws InterruptedException {
        // given
        String url = "http://test.com";

        // when
        Thread.sleep(500);
        hitTrackerService.hit(url);

        // then
        assertThat(hitRepository.getTodayHit(url)).isEqualTo(1);
        assertThat(hitRepository.getTotalHit(url)).isEqualTo(1);
    }

    @Test
    void getStatistics() {
        // given
        String url = "http://test.com";
        Url savedUrl = urlRepository.save(new Url(url));
        DailyHitLog savedLog = dailyHitLogRepository.save(new DailyHitLog(LocalDate.of(2020, 1, 1), 3, savedUrl));

        // when
        List<DailyHitLog> dailyHitLogs = hitTrackerService.getStatistics(url);

        // then
        assertThat(dailyHitLogs.size()).isEqualTo(1);
        assertThat(dailyHitLogs.get(0)).isEqualTo(savedLog);
    }

    @Test
    void tomorrow() {
        // given
        String url = "http://test.com";
        Url savedUrl = urlRepository.save(new Url(url));
        hitRepository.hit(url);

        LocalDate NOW = LocalDate.of(2020, 1, 9);
        ZoneId zone = ZoneOffset.systemDefault();
        Instant fixedInstant = NOW.atStartOfDay(zone).toInstant();
        Clock mockClock = Clock.fixed(fixedInstant, zone);

        DailyHitLog givenToBeDeleted = dailyHitLogRepository.save(new DailyHitLog(LocalDate.of(2020, 1, 1), 0, savedUrl));
        DailyHitLog givenToBeRemaining = dailyHitLogRepository.save(new DailyHitLog(LocalDate.of(2020, 1, 2), 0, savedUrl));

        // when
        hitTrackerService.tomorrow(mockClock);

        // then
        assertThat(hitRepository.getTodayHit(url)).isEqualTo(0);
        assertThat(hitRepository.getTotalHit(url)).isEqualTo(1);

        List<DailyHitLog> dailyHitLogs = dailyHitLogRepository.findAllByUrl(urlRepository.findByUrl(url).orElseThrow());
        assertThat(dailyHitLogs.size()).isEqualTo(2);
        assertThat(dailyHitLogs).contains(givenToBeRemaining);
        assertThat(dailyHitLogs).doesNotContain(givenToBeDeleted);

        DailyHitLog newLog = dailyHitLogs.stream().filter(log -> log != givenToBeRemaining).findFirst().orElseThrow();
        assertThat(newLog.getDailyHit()).isEqualTo(1);
        assertThat(newLog.getDate()).isEqualTo(NOW.minusDays(1));   
    }
}

@TestConfiguration
class TestRedisConfiguration {

    private final RedisServer redisServer;

    public TestRedisConfiguration() throws IOException {
        this.redisServer = new RedisServer(6380);
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        redisServer.stop();
    }
}
