package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.UserEntity;
import com.first.bulletinboard.domain.dto.UserDto;
import com.first.bulletinboard.domain.dto.UserJoinRequest;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.UserRepository;
import com.first.bulletinboard.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String key;
    private Long expiredTime = 1000 * 60 * 60L; // 1hour

    public UserDto join(UserJoinRequest request) {
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user->{ // userName 중복 체크
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, // 에러 발생하면 RestControllerAdvice에서 처리
                            String.format("해당 userName %s 는 이미 있습니다.",request.getUserName()));
                });

        // save + password encrypt
        UserEntity savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .userName(savedUser.getUserName())
                .password(savedUser.getPassword())
                .build();
    }

    public String login(String userName, String password){
        UserEntity selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, userName+"이 없습니다."));
        if(!encoder.matches(password, selectedUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, password+"가 틀렸습니다.");
        }
        String token = JwtTokenUtil.createToken(selectedUser.getUserName(), key, expiredTime);
        return token;
    }

}
