package com.renew.sw.mentoring.domain.user.service;


import com.renew.sw.mentoring.domain.user.exception.AlreadyNicknameException;
import com.renew.sw.mentoring.domain.user.exception.UserNotFoundException;
import com.renew.sw.mentoring.domain.user.exception.WrongPasswordException;
import com.renew.sw.mentoring.domain.user.model.UserRole;
import com.renew.sw.mentoring.domain.user.model.dto.request.RequestExistPasswordChangeDto;
import com.renew.sw.mentoring.domain.user.model.dto.request.RequestLoginDto;
import com.renew.sw.mentoring.domain.user.model.dto.request.RequestNickNameChangeDto;
import com.renew.sw.mentoring.domain.user.model.dto.request.RequestSignupDto;
import com.renew.sw.mentoring.domain.user.model.dto.response.ResponseLoginDto;
import com.renew.sw.mentoring.domain.user.model.entity.User;
import com.renew.sw.mentoring.domain.user.repository.UserRepository;
import com.renew.sw.mentoring.global.auth.jwt.AuthenticationToken;
import com.renew.sw.mentoring.global.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // TODO : UserService 코드 구체화

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    private final UserInfoService userInfoService;

    public void signup(RequestSignupDto dto) {
        User user = User.builder()
                .studentId(dto.getStudentId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .nickname(createRandomNickname())
                .userRole(UserRole.MENTEE)
                .build();

        userRepository.save(user);
    }

    public ResponseLoginDto login(String studentId, String password) {
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        AuthenticationToken token = jwtProvider.issue(user);
        return new ResponseLoginDto(token);
    }

    private String createRandomNickname() {
        List<String> adjectives = new ArrayList<>();
        List<String> celebrity = new ArrayList<>();

        // 파일에서 형용사들을 읽어와 리스트에 저장
        try (InputStream inputStream = getClass().getResourceAsStream("/adjectives.txt")) {
            assert inputStream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    adjectives.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일에서 유명인들을 읽어와 리스트에 저장
        try (InputStream inputStream = getClass().getResourceAsStream("/celebrity.txt")) {
            assert inputStream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    celebrity.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 랜덤 객체 생성
        Random random = new Random();

        String result;
        while(true) {
            String randomAdjective = adjectives.get(random.nextInt(adjectives.size()));
            String randomCelebrity = celebrity.get(random.nextInt(celebrity.size()));

            result = randomAdjective + " " + randomCelebrity;

            if (userRepository.findByNickname(result).isEmpty()) break;
        }

        return result;
    }
}
