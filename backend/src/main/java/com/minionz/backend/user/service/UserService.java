package com.minionz.backend.user.service;

import com.minionz.backend.common.domain.Message;
import com.minionz.backend.common.exception.BadRequestException;
import com.minionz.backend.common.exception.NotEqualsException;
import com.minionz.backend.common.exception.NotFoundException;
import com.minionz.backend.user.controller.dto.UserJoinRequest;
import com.minionz.backend.user.controller.dto.UserLoginRequestDto;
import com.minionz.backend.user.controller.dto.UserRequestDto;
import com.minionz.backend.user.domain.User;
import com.minionz.backend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String NO_SUCH_USER_MESSAGE = "해당 유저가 존재하지 않습니다.";
    private static final String NO_FOUND_USER_EMAIL_MESSAGE = "이메일에 해당하는 유저가 존재하지 않습니다.";
    private static final String NOT_EQUALS_PASSWORD_MESSAGE = "비밀번호가 일치하지 않습니다.";
    private static final String NOT_ENTERED_EMAIL = "이메일이 입력되지 않았습니다.";
    private static final String LOGIN_SUCCESS = "로그인 성공";
    private final UserRepository userRepository;

    public Message login(UserLoginRequestDto userLoginRequestDto) {
        System.out.println(userLoginRequestDto.getEmail());
        validateEnteredEmail(userLoginRequestDto);
        User findUser = userRepository.findByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(() -> new NotFoundException(NO_FOUND_USER_EMAIL_MESSAGE));
        validatePassword(userLoginRequestDto, findUser);
        return new Message(LOGIN_SUCCESS);
    }

    public Message logout(UserRequestDto userRequestDto) {
        return null;
    }

    public Message signUp(UserJoinRequest userJoinRequest) {
        return null;
    }

    public Message withdraw(UserRequestDto userRequestDto) {
        return null;
    }

    private void validateEnteredEmail(UserLoginRequestDto userLoginRequestDto) {
        if ("".equals(userLoginRequestDto.getEmail())) {
            throw new BadRequestException(NOT_ENTERED_EMAIL);
        }
    }

    private void validatePassword(UserLoginRequestDto userLoginRequestDto, User findUser) {
        if (!findUser.validatePassword(userLoginRequestDto.getPassword())) {
            throw new NotEqualsException(NOT_EQUALS_PASSWORD_MESSAGE);
        }
    }
}
