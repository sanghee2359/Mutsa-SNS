package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.UserEntity;
import com.first.bulletinboard.domain.dto.UserDto;
import com.first.bulletinboard.domain.dto.UserJoinRequest;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest request) {
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user->{ // userName 중복 체크
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, // 에러 발생하면 RestControllerAdvice에서 처리
                            String.format("해당 userName %s 는 이미 있습니다.",request.getUserName()));
                });

        // save
        UserEntity savedUser = userRepository.save(request.toEntity(request.getPassword()));
        return UserDto.builder()
                .userName(savedUser.getUserName())
                .password(savedUser.getPassword())
                .build();
    }

}
