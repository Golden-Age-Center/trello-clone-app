package com.sparta.trelloproject.domain.notification.entity;

import com.sparta.trelloproject.common.entity.Timestamped;
import com.sparta.trelloproject.domain.notification.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor
public class Notification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String content;

    @NotNull
    private Long targetId; //대상

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationType targetType;

    public Notification(Long targetId,String content, NotificationType targetType) {
        this.targetId = targetId;
        this.content = content;
        this.targetType = targetType;
    }
}
