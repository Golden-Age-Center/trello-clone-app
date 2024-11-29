package com.sparta.trelloproject.domain.comment.dto.response;

import com.sparta.trelloproject.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SaveCommentResponseDto {
    private Long commentId;
    private Long userId;
    private Long cardId;
    private String contents;
    private LocalDateTime createAt;

    private SaveCommentResponseDto(Long commentId, Long userId, Long cardId, String contents, LocalDateTime createAt){
        this.commentId=commentId;
        this.userId=userId;
        this.cardId=cardId;
        this.contents=contents;
        this.createAt=createAt;
    }

    public static SaveCommentResponseDto from(Comment comment){
        return new SaveCommentResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getCard().getId(),
                comment.getContents(),
                comment.getCreatedAt()
        );
    }
}
