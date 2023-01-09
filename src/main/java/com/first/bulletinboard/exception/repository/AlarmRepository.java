package com.first.bulletinboard.exception.repository;

import com.first.bulletinboard.domain.entity.alarm.Alarm;
import com.first.bulletinboard.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    Page<Alarm> findAlarmsByUser(User user, Pageable pageable);
}
