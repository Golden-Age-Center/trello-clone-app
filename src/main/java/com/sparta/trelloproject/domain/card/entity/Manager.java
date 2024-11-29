package com.sparta.trelloproject.domain.card.entity;

import com.sparta.trelloproject.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manager")
@Getter
@NoArgsConstructor
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cards_id")
    private Card card;

    private Manager(User user, Card card) {
        this.user = user;
        this.card = card;
    }

    public static Manager from(User user, Card card) {
        return new Manager(user, card);
    }
}
