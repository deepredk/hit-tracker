package it.numble.hittracker.service;

import it.numble.hittracker.common.config.redis.RedisProperties;
import it.numble.hittracker.controller.response.UrlHitInfoResponse;
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
