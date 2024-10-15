package com.sparta.trelloproject.domain.comment.controller;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.response.SuccessResponse;
import com.sparta.trelloproject.domain.comment.dto.request.SaveCommentRequest;
import com.sparta.trelloproject.domain.comment.dto.request.UpdateCommentRequest;
import com.sparta.trelloproject.domain.comment.dto.response.SaveCommentResponse;
import com.sparta.trelloproject.domain.comment.dto.response.UpdateCommentResponse;
import com.sparta.trelloproject.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentService commentService;

    //댓글 등록
    @PostMapping("/cards/{cardId}/comment")
    public ResponseEntity<SuccessResponse<SaveCommentResponse>>saveComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("cardId")Long cardId,
            @Valid @RequestBody SaveCommentRequest saveCommentRequest){

        SaveCommentResponse saveCommentResponse=commentService.saveComment(authUser,cardId,saveCommentRequest);
        return ResponseEntity.ok(SuccessResponse.of(saveCommentResponse));
    }

    //댓글 수정
    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<SuccessResponse<UpdateCommentResponse>> updateComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody UpdateCommentRequest updateCommentRequest){
        UpdateCommentResponse updateCommentResponse=commentService.updateComment(authUser,commentId,updateCommentRequest);
        return ResponseEntity.ok(SuccessResponse.of(updateCommentResponse));

    }

    //댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<SuccessResponse<Void>> deleteComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("commentId") Long commentId
    ){
        commentService.deleteComment(authUser,commentId);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }
}
