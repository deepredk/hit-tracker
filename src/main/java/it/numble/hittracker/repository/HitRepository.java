package it.numble.hittracker.repository;

import it.numble.hittracker.entity.Url;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HitRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void hit(String url) {
        redisTemplate
            .opsForValue()
            .increment(url + ".todayHit");

        redisTemplate
            .opsForValue()
            .increment(url + ".totalHit");
    }

    public int getTodayHit(String url) {
        redisTemplate.opsForValue().setIfAbsent(url + ".todayHit", "0");
        return Integer.parseInt(redisTemplate.opsForValue().get(url + ".todayHit"));
    }

    public int getTotalHit(String url) {
        redisTemplate.opsForValue().setIfAbsent(url + ".totalHit", "0");
        return Integer.parseInt(redisTemplate.opsForValue().get(url + ".totalHit"));
    }

    public void deleteTodayHit(String url) {
        redisTemplate.opsForValue().getAndDelete(url + ".todayHit");
    }

    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
