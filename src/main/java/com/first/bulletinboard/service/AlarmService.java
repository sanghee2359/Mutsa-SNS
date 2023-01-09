package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.dto.alarm.AlarmDto;
import com.first.bulletinboard.domain.entity.alarm.Alarm;
import com.first.bulletinboard.domain.entity.alarm.AlarmType;
import com.first.bulletinboard.domain.entity.comment.Comment;
import com.first.bulletinboard.domain.entity.like.Like;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.AlarmRepository;
import com.first.bulletinboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    // create
    @Transactional
    public void createCommentAlarm(Comment comment){
        Alarm alarm = Alarm.builder().
                alarmType(AlarmType.NEW_LIKE_ON_POST).
                user(comment.getPost().getUser()).
                fromUserId(comment.getUser().getId()).
                targetId(comment.getPost().getId()).
                text("new comment!").
                build();

        alarmRepository.save(alarm);
    }
    @Transactional
    public void createLikeAlarm(Like like){
        Alarm alarm = Alarm.builder().
                alarmType(AlarmType.NEW_LIKE_ON_POST).
                user(like.getPost().getUser()).
                fromUserId(like.getUser().getId()).
                targetId(like.getPost().getId()).
                text("new like!").
                build();
        log.info("postid:{}", alarm.getTargetId());
        alarmRepository.save(alarm);
    }

    // read
    public Page<AlarmDto> findAllAlarms(String userName, Pageable pageable){
        User user = findUserByUserName(userName);
        return alarmRepository.findAlarmsByUser(user,pageable);
    }
    public User findUserByUserName(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(()-> {
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
    }
}
