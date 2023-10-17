package it.numble.hittracker.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IsCreatedUrlRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Long create(String url) {
        return redisTemplate
                .opsForSet()
                .add("created_url", url);
    }
}
