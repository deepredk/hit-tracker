package it.numble.hittracker.repository;

import it.numble.hittracker.entity.Url;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HitRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public HitRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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
}
