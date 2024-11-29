package com.sparta.trelloproject.domain.comment.entity;

import com.sparta.trelloproject.common.entity.Timestamped;
import com.sparta.trelloproject.domain.card.entity.Card;
import com.sparta.trelloproject.domain.comment.dto.request.SaveCommentRequestDto;
import com.sparta.trelloproject.domain.comment.dto.request.UpdateCommentRequestDto;
import com.sparta.trelloproject.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "comments")
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "댓글 내용은 공백이 올 수 없습니다.")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cards_id")
    private Card card;

    private Comment(String contents,User user,Card card){
        this.contents=contents;
        this.user=user;
        this.card=card;
    }
    public static Comment from(SaveCommentRequestDto saveCommentRequestDto, User user, Card card){
        return new Comment(
                saveCommentRequestDto.getContents(),
                user,
                card
        );
    }

    public void update(UpdateCommentRequestDto updateCommentRequest){
        this.contents=updateCommentRequest.getContents();
    }
}
