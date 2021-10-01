package com.minionz.backend.user.controller;

import com.minionz.backend.common.domain.StatusCode;
import com.minionz.backend.user.controller.dto.*;
import com.minionz.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        try {
            UserLoginResponseDto userLoginResponseDto = userService.login(userLoginRequestDto);
            userLoginResponseDto.setStatusCode(StatusCode.OK);
            return userLoginResponseDto;
        } catch (Exception e) {
            UserLoginResponseDto userLoginResponseDtoFail = new UserLoginResponseDto("null");
            userLoginResponseDtoFail.setStatusCode(StatusCode.BAD_REQUEST);
            return userLoginResponseDtoFail;
        }
    }

    @GetMapping("/logout/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserLogoutResponseDto logout(@PathVariable("email") String email) {
        try {
            UserLogoutResponseDto user = userService.logout(email);
            user.setStatusCode(StatusCode.OK);
            return user;
        } catch (Exception e) {
            UserLogoutResponseDto userLogoutResponseDto = new UserLogoutResponseDto(email);
            userLogoutResponseDto.setStatusCode(StatusCode.BAD_REQUEST);
            return userLogoutResponseDto;
        }
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public UserJoinResponse singUp(@RequestBody UserJoinRequest userJoinRequest) {
        return userService.signUp(userJoinRequest);
    }

    @DeleteMapping("/withdraw/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserWithdrawResponse withdraw(@PathVariable("email") String email) {
        return userService.withdraw(email);
    }
}
