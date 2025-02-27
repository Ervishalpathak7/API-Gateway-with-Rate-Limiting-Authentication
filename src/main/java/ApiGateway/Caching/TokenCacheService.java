package ApiGateway.Caching;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public TokenCacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheToken(String token, String username) {
        // 1 hour
        long TOKEN_EXPIRATION_SECONDS = 3600;
        redisTemplate.opsForValue().set(token, username, TOKEN_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public String getUsernameFromCache(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void invalidateToken(String token) {
        redisTemplate.delete(token);
    }
}
