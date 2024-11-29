package com.sparta.trelloproject.domain.comment.dto.response;

import com.sparta.trelloproject.domain.comment.entity.Comment;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UpdateCommentResponseDto {
    private Long commentId;
    private Long userId;
    private Long cardId;
    private String contents;
    private LocalDateTime modifiedAt;

    private UpdateCommentResponseDto(Long commentId, Long userId, Long cardId, String contents, LocalDateTime modifiedAt){
        this.commentId=commentId;
        this.userId=userId;
        this.cardId=cardId;
        this.contents=contents;
        this.modifiedAt=modifiedAt;
    }

    public static UpdateCommentResponseDto from(Comment comment){
        return new UpdateCommentResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getCard().getId(),
                comment.getContents(),
                comment.getModifiedAt()
        );
    }
}
