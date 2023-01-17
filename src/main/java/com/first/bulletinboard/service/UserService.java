package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.domain.dto.user.UserDto;
import com.first.bulletinboard.domain.dto.user.UserJoinRequest;
import com.first.bulletinboard.domain.entity.user.UserRole;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.UserRepository;
import com.first.bulletinboard.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.first.bulletinboard.exception.ErrorCode.INVALID_PASSWORD;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}") // spring에서 지원하는 어쩌고
    private String secretKey;
    private Long expireTimeMs = 1000 * 60 * 60L; // 1초 * 60 * 60 = 1hour

    // username을 가지고 UserDetails 객체 리턴
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<User> user = userRepository.findByUserName(username);

        if(user.isPresent()) {
            User roleUser = user.get();
            User authUser = User.builder()
                    .id(roleUser.getId())
                    .userName(roleUser.getUsername())
                    .password(roleUser.getPassword())
                    .role(roleUser.getRole())
                    .updatedAt(roleUser.getUpdatedAt())
                    .removedAt(roleUser.getRemovedAt())
                    .createdAt(roleUser.getCreatedAt())
                    .build();
            log.info("user details User :{}",authUser.getUsername());
            return authUser;
        }
        return null;
    }
    @Transactional
    public UserDto join(UserJoinRequest request) {
        // userName 중복
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user->{
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME);
        });
        // 역할 부여
        String userName = request.getUserName();
        String password = request.getPassword();
        User user;

        // ADMIN 회원을 생성할 때 필수 조건
        if(userName.equals("admin") && password.equals("admin")){
            user = request.toEntity(encoder.encode(password)
                    , UserRole.ADMIN);
        } // ADMIN -> USER
        else {
            user = request.toEntity(encoder.encode(password)
                    , UserRole.USER);
        }

        // 바뀐 정보 저장 및 dto 변환
        User savedUser = userRepository.save(user);
        return savedUser.toUserDto();
    }

    public String login(String userName, String password) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        if(!encoder.matches(password, user.getPassword())){
            throw new AppException(INVALID_PASSWORD);
        }
        return JwtTokenUtil.generateToken(user,secretKey,expireTimeMs);
    }

    public UserDto roleChange(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        if(user.getRole() == UserRole.USER) {
            user.setRole(UserRole.ADMIN);

            userRepository.save(user);
        }else if(user.getRole() == UserRole.ADMIN) {
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }
        return user.toUserDto();
    }

    public Page<UserDto> findAllUser(Pageable pageable) {
        log.info("find users in service ");
        return userRepository.findAll(pageable).map(User::toUserDto);
    }
}