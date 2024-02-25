package com.renew.sw.mentoring.domain.user.controller;

import com.renew.sw.mentoring.domain.user.model.dto.request.RequestExistPasswordChangeDto;
import com.renew.sw.mentoring.domain.user.model.dto.request.RequestLoginDto;
import com.renew.sw.mentoring.domain.user.model.dto.request.RequestNickNameChangeDto;
import com.renew.sw.mentoring.domain.user.model.dto.request.RequestSignupDto;
import com.renew.sw.mentoring.domain.user.model.dto.response.ResponseLoginDto;
import com.renew.sw.mentoring.domain.user.model.dto.response.ResponseUserInfoDto;
import com.renew.sw.mentoring.domain.user.service.UserInfoService;
import com.renew.sw.mentoring.domain.user.service.UserService;
import com.renew.sw.mentoring.global.auth.jwt.AppAuthentication;
import com.renew.sw.mentoring.global.auth.role.AdminAuth;
import com.renew.sw.mentoring.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "유저", description = "유저 관련 api")
public class UserController {

    private final UserService userService;
    private final UserInfoService userInfoService;

    /**
     * 내 정보 조회
     * 
     * @return 내 정보
     */
    @GetMapping
    @UserAuth
    public ResponseUserInfoDto getMyInfo(AppAuthentication auth) {
        return userInfoService.getFullUserInfo(auth.getUserId());
    }

    /**
     * 로그인
     *
     * @param dto 요청 body
     * @return 로그인 인증 정보
     */
    @PostMapping("/login")
    public ResponseLoginDto login(@RequestBody RequestLoginDto dto) {
        return userService.login(dto);
    }

    /**
     * 닉네임 변경
     *
     * @param dto 요청 body
     */
    @PatchMapping("/change/nickname")
    @UserAuth
    public void changeNickName(AppAuthentication auth,
                               @Valid @RequestBody RequestNickNameChangeDto dto) {
        userService.changeNickname(auth.getUserId(), dto);
    }

    /**
     * 비밀번호 변경 - 기존 비밀번호를 알고 있는 경우
     *
     * @param dto 요청 body
     */
    @PatchMapping("/change/password")
    @UserAuth
    public void changePassword(AppAuthentication auth,
                               @Valid @RequestBody RequestExistPasswordChangeDto dto) {
        userService.changePassword(auth.getUserId(), dto);
    }

    /**
     * 비밀번호 초기화(관리자 권한)
     *
     * @param studentId 비밀번호를 초기화 할 학번
     */
    @PatchMapping("/reset/password")
    @AdminAuth
    public void resetPassword(@RequestParam("studentId") String studentId) {
        userService.resetPassword(studentId);
    }
}
