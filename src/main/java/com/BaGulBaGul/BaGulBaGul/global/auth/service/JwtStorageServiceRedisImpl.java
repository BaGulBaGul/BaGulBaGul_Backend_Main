package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredRefreshTokenException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class JwtStorageServiceRedisImpl implements JwtStorageService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOperations;

    public static final String REDIS_KEY_PREFIX = "AT_";

    public JwtStorageServiceRedisImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public void save(AccessTokenInfo accessTokenInfo, RefreshTokenInfo refreshTokenInfo) {
        //RT가 만료될 때까지 ttl을 적용
        long ttl = ChronoUnit.SECONDS.between(new Date().toInstant(), refreshTokenInfo.getExpireAt().toInstant());
        if(ttl < 0) {
            throw new ExpiredRefreshTokenException();
        }
        valueOperations.set(
                getKey(accessTokenInfo),
                getValue(refreshTokenInfo),
                Duration.ofSeconds(ttl)
        );
    }

    @Override
    public boolean checkPresentAndDelete(AccessTokenInfo accessTokenInfo, RefreshTokenInfo refreshTokenInfo) {
        String result = valueOperations.getAndDelete(getKey(accessTokenInfo));
        if(result == null) {
            return false;
        }
        return getValue(refreshTokenInfo).equals(result);
    }

    @Override
    public void delete(AccessTokenInfo accessTokenInfo) {
        redisTemplate.delete(getKey(accessTokenInfo));
    }

    public String getKey(AccessTokenInfo accessTokenInfo) {
        return REDIS_KEY_PREFIX + accessTokenInfo.getJti();
    }

    public String getValue(RefreshTokenInfo refreshTokenInfo) {
        return refreshTokenInfo.getJti();
    }
}
