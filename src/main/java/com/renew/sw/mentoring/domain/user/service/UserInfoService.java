package com.renew.sw.mentoring.domain.user.service;

import com.renew.sw.mentoring.domain.user.exception.UserNotFoundException;
import com.renew.sw.mentoring.domain.user.model.UserInfo;
import com.renew.sw.mentoring.domain.user.model.dto.response.ResponseUserInfoDto;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserInfoMemoryRepository;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final Clock clock;
    private final UserRepository persistenceRepository;
    private final UserInfoMemoryRepository memoryRepository;

    @Transactional
    public ResponseUserInfoDto getFullUserInfo(Long userId) {
        User user = persistenceRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return new ResponseUserInfoDto(user.getStudentId(), user.getName(), user.getNickname(), user.getTeam().getTeamName(), user.getUserRole().isAdmin());
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfo(Long userId) {
        Instant now = Instant.now(clock);
        return memoryRepository.getUserInfo(userId, now)
                .orElseGet(() -> {
                    User user = persistenceRepository.findById(userId)
                            .orElseThrow(UserNotFoundException::new);
                    UserInfo userInfo = new UserInfo(user);
                    memoryRepository.setUserInfo(userId, userInfo, now);
                    return userInfo;
                });
    }

    public void invalidateUserInfo(Long userId) {
        memoryRepository.removeUserInfo(userId);
    }

    public void cacheUserInfo(Long userId, User user) {
        UserInfo userInfo = new UserInfo(user);
        memoryRepository.setUserInfo(userId, userInfo, Instant.now(clock));
    }
}
