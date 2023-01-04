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
import org.springframework.data.domain.PageRequest;
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
    // userDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        //adminUser 정보 조회
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
                    .registeredAt(roleUser.getRegisteredAt())
                    .build();

            log.info("auth : {}", authUser);
            return authUser;
        }
        return null;
    }
    @Transactional
    public UserDto join(UserJoinRequest request) {
        // 비즈니스 로직 - 회원 가입
        // 회원 userName 중복 check
        // 중복되면 회원가입 불가능 exception발생
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user->{
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME);
        });
        // 역할 부여
        String userName = request.getUserName();
        String password = request.getPassword();
        User user;
        if(userName.equals("admin") && password.equals("admin")){ // 최초 관리자 권한
            user = request.toEntity(encoder.encode(password)
                    , UserRole.ADMIN);
        }else {
            user = request.toEntity(encoder.encode(password)
                    , UserRole.USER);
        }

        // 회원가입 .save()
        User savedUser = userRepository.save(user);
        return savedUser.toUserDto();
    }

    public String login(String userName, String password) {
        // userName 존재 여부 확인
        // 없다면 NOT-FOUND 에러
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        // password 일치 여부 확인
        if(!encoder.matches(password, user.getPassword())){
            throw new AppException(INVALID_PASSWORD);
        }
        // 두 가지 확인 도중 예외가 안났다면 token 발행
        return JwtTokenUtil.createToken(userName,secretKey,expireTimeMs);
    }

    public UserDto roleChange(int id) {
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

    public Page<UserDto> findAllUser() {

        PageRequest pageRequest = PageRequest.of(0, 20);
        return userRepository.findAll(pageRequest).map(User::toUserDto);
    }
}