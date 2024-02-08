package com.renew.sw.mentoring.domain.user.service;


import com.renew.sw.mentoring.domain.user.model.UserRole;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // TODO : UserService 코드 구체화

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public void signup(RequestSignupDto dto) {
        User user = User.builder()
                .studentId(dto.getStudentId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .nickname(dto.getNickname())
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
}
