package com.sparta.trelloproject.domain.user.dto.response;

import com.sparta.trelloproject.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorityUpdateResponseDto {
    private long id;
    private String email;
    private String name;
    private String authority;

    public static UserAuthorityUpdateResponseDto from(User user) {
        return new UserAuthorityUpdateResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getAuthority().getUserRole()
        );
    }
}
