package com.sparta.trelloproject.domain.notification.repository;

import com.sparta.trelloproject.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationQueryRepository {
}
