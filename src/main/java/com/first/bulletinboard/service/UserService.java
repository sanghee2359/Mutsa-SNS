package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.domain.dto.user.UserDto;
import com.first.bulletinboard.domain.dto.user.UserJoinRequest;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.UserRepository;
import com.first.bulletinboard.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));
        return null;
        /*List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (Role.ADMIN.equals(member.getRoleType())) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }*/
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

        // 회원가입 .save()
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
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
}