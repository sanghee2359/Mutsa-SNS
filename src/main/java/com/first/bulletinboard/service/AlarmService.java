package com.first.bulletinboard.service;

import com.first.bulletinboard.domain.dto.alarm.AlarmDto;
import com.first.bulletinboard.domain.entity.alarm.Alarm;
import com.first.bulletinboard.domain.entity.alarm.AlarmType;
import com.first.bulletinboard.domain.entity.comment.Comment;
import com.first.bulletinboard.domain.entity.like.Like;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public Page<AlarmDto> findAllAlarms(String userName){
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->{
                    throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
                });
        PageRequest pageRequest = PageRequest.of(0,20, Sort.by("createdAt").descending());
        Page<AlarmDto> dto = alarmRepository.findAlarmsByUser(user, pageRequest).map(Alarm::toAlarmDto);
        return dto;
    }
    public void createCommentAlarm(Comment comment){
        Alarm alarm = Alarm.builder().
                alarmType(AlarmType.NEW_COMMENT_ON_POST).
                user(comment.getPost().getUser()).
                fromUserId(comment.getUser().getId()).
                targetId(comment.getPost().getId()).
                text("new like!").
                build();
        alarmRepository.save(alarm);
    }

    public void createLikeAlarm(Like like){
        Alarm alarm = Alarm.builder().
                alarmType(AlarmType.NEW_LIKE_ON_POST).
                user(like.getPost().getUser()).
                fromUserId(like.getUser().getId()).
                targetId(like.getPost().getId()).
                text("new like!").
                build();
        alarmRepository.save(alarm);
//        return savedAlarm.;
    }

}
