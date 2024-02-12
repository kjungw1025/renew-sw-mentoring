package com.renew.sw.mentoring.domain.user.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renew.sw.mentoring.domain.user.model.UserInfo;
import com.renew.sw.mentoring.domain.user.repository.UserInfoMemoryRepository;
import com.renew.sw.mentoring.global.base.AbstractKeyValueCacheRepository;
import com.renew.sw.mentoring.global.config.redis.RedisKeys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Repository
public class UserInfoRedisRepository extends AbstractKeyValueCacheRepository implements UserInfoMemoryRepository {

    private final Duration cacheDuration;

    protected UserInfoRedisRepository(StringRedisTemplate redisTemplate,
                                      ObjectMapper objectMapper,
                                      @Value("${app.user.info-cache-time}") Duration cacheDuration) {
        super(redisTemplate, objectMapper, RedisKeys.USER_INFO_CACHE_KEY);
        this.cacheDuration = cacheDuration;
    }

    @Override
    public Optional<UserInfo> getUserInfo(Long userId, Instant now) {
        return get(userId.toString(), UserInfo.class, now);
    }

    @Override
    public void setUserInfo(Long userId, UserInfo userInfo, Instant now) {
        set(userId.toString(), userInfo, now, cacheDuration);
    }

    @Override
    public void removeUserInfo(Long userId) {
        remove(userId.toString());
    }
}
