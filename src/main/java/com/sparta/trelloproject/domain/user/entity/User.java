package com.sparta.trelloproject.domain.user.entity;

import com.sparta.trelloproject.common.entity.Timestamped;
import com.sparta.trelloproject.domain.auth.dto.request.UserSignUpRequestDto;
import com.sparta.trelloproject.domain.user.dto.request.UserAuthorityUpdateRequestDto;
import com.sparta.trelloproject.domain.user.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private boolean isDeleted = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole authority;

    private User(String email, String password, String name, UserRole authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.authority = authority;
    }

    public static User from(String encryptPassword, UserSignUpRequestDto userSignUpRequestDto) {
        return new User(
                userSignUpRequestDto.getEmail(),
                encryptPassword,
                userSignUpRequestDto.getName(),
                UserRole.of(userSignUpRequestDto.getAuthority())
        );
    }

    public void updateUserAuthority(UserAuthorityUpdateRequestDto userRoleUpdateRequestDto) {
        this.authority = UserRole.of(userRoleUpdateRequestDto.getAuthority());
    }

    public void deleteUser() {
        this.isDeleted = true;
    }

}
