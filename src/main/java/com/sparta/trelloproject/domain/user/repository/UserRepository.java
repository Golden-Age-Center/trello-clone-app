package com.sparta.trelloproject.domain.user.repository;

import com.sparta.trelloproject.common.exception.InvalidParameterException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.sparta.trelloproject.common.exception.ResponseCode.NOT_FOUND_USER;
import static com.sparta.trelloproject.common.exception.ResponseCode.WRONG_EMAIL_OR_PASSWORD;

public interface UserRepository extends JpaRepository<User, Long>, UserQueryRepository {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    default User findByUserId(long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

    default User findByUserEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new InvalidParameterException(WRONG_EMAIL_OR_PASSWORD));
    }

}
