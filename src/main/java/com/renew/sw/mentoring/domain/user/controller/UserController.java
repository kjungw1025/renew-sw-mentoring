package com.renew.sw.mentoring.domain.user.controller;

import com.renew.sw.mentoring.domain.user.model.dto.request.RequestLoginDto;
import com.renew.sw.mentoring.domain.user.model.dto.request.RequestSignupDto;
import com.renew.sw.mentoring.domain.user.model.dto.response.ResponseLoginDto;
import com.renew.sw.mentoring.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signUp(@RequestBody RequestSignupDto dto) {
        userService.signup(dto);
    }

    @GetMapping("/login")
    public ResponseLoginDto login(@RequestBody RequestLoginDto dto) {
        return userService.login(dto.getStudentId(), dto.getPassword());
    }
}
