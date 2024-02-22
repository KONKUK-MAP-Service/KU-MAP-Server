package com.cona.KUsukKusuk.user.controller;

import com.cona.KUsukKusuk.global.response.HttpResponse;
import com.cona.KUsukKusuk.global.security.JWTUtil;
import com.cona.KUsukKusuk.user.domain.User;
import com.cona.KUsukKusuk.user.dto.TokenRefreshRequest;
import com.cona.KUsukKusuk.user.dto.TokenRefreshResponse;
import com.cona.KUsukKusuk.user.dto.UserJoinRequest;
import com.cona.KUsukKusuk.user.dto.UserJoinResponse;
import com.cona.KUsukKusuk.user.dto.UserLogoutResponse;
import com.cona.KUsukKusuk.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/join")
    public HttpResponse<UserJoinResponse> join(@Valid @RequestBody UserJoinRequest userJoinRequest) {
        User savedUser = userService.save(userJoinRequest);
        return HttpResponse.okBuild(
                UserJoinResponse.of(savedUser)
        );
    }
    @PatchMapping("/logout")
    public HttpResponse<UserLogoutResponse> logout(HttpServletRequest request) {

        String username= SecurityContextHolder.getContext().getAuthentication()
                .getName();
        String encryptedRefreshToken = jwtUtil.getRefreshToken(request);
        String accessToken = jwtUtil.getAccessToken(request);
        String blacklist = userService.logout(encryptedRefreshToken, accessToken);

        return HttpResponse.okBuild(
                UserLogoutResponse.from(username,blacklist)
        );
    }
    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "만료된 AccessToken을 RefreshToken을 사용해 갱신합니다.")
    public HttpResponse<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest refreshRequest) {
        String newAccessToken = userService.refreshToken(refreshRequest.getRefreshToken());
        return HttpResponse.okBuild(
                TokenRefreshResponse.of(newAccessToken)
        );
    }
}
