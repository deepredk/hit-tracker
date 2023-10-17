package it.numble.hittracker.service;

import it.numble.hittracker.common.config.redis.RedisProperties;
import it.numble.hittracker.controller.response.UrlHitInfoResponse;
import it.numble.hittracker.entity.DailyHitLog;
import it.numble.hittracker.entity.Url;
import it.numble.hittracker.repository.DailyHitLogRepository;
import it.numble.hittracker.repository.HitRepository;
import it.numble.hittracker.repository.UrlRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        UrlHitInfoResponse hitInfo = hitTrackerService.getHitInfo(url);

        // then
        assertThat(hitInfo.getTodayHit()).isEqualTo(1);
        assertThat(hitInfo.getTotalHit()).isEqualTo(1);
    }

    @Test
    void hit() {
        // given
        String url = "http://test.com";

        // when
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
}

@TestConfiguration
class TestRedisConfiguration {

    private final RedisServer redisServer;

    public TestRedisConfiguration(RedisProperties redisProperties) {
        this.redisServer = new RedisServer(redisProperties.getRedisPort());
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}
