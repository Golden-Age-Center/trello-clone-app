package com.sparta.trelloproject.domain.comment.service;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.exception.ForbiddenException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import com.sparta.trelloproject.common.exception.UnauthorizedException;
import com.sparta.trelloproject.domain.card.entity.Card;
import com.sparta.trelloproject.domain.card.entity.Manager;
import com.sparta.trelloproject.domain.card.repository.CardRepository;
import com.sparta.trelloproject.domain.card.repository.ManagerRepository;
import com.sparta.trelloproject.domain.comment.dto.request.SaveCommentRequestDto;
import com.sparta.trelloproject.domain.comment.dto.request.UpdateCommentRequestDto;
import com.sparta.trelloproject.domain.comment.dto.response.SaveCommentResponseDto;
import com.sparta.trelloproject.domain.comment.dto.response.UpdateCommentResponseDto;
import com.sparta.trelloproject.domain.comment.entity.Comment;
import com.sparta.trelloproject.domain.comment.repository.CommentRepository;
import com.sparta.trelloproject.domain.notification.enums.NotificationMessage;
import com.sparta.trelloproject.domain.notification.enums.NotificationType;
import com.sparta.trelloproject.domain.notification.event.SavedCommentEvent;
import com.sparta.trelloproject.domain.notification.service.NotificationService;
import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.user.repository.UserRepository;
import com.sparta.trelloproject.domain.workspace.entity.UserWorkspace;
import com.sparta.trelloproject.domain.workspace.entity.Workspace;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import com.sparta.trelloproject.domain.workspace.repository.WorkspaceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.trelloproject.common.exception.ResponseCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserWorkSpaceRepository userWorkSpaceRepository;
    private final NotificationService notificationService;

    private final ApplicationEventPublisher eventPublisher;

    //댓글 등록
    @Transactional
    public SaveCommentResponseDto saveComment(AuthUser authUser, Long cardId, SaveCommentRequestDto saveCommentRequestDto) {
        User user=userRepository.findById(authUser.getUserId()).orElseThrow(()->
                new NotFoundException(NOT_FOUND_USER));

        //워크스페이스가 있는 지 확인
        Workspace workspace = workspaceRepository.findById(saveCommentRequestDto.getWorkspaceId())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_WORKSPACE));

        //읽기 전용 유저인지 확인
        UserWorkspace userWorkspace = userWorkSpaceRepository.findByWorkspaceIdAndUserId(saveCommentRequestDto.getWorkspaceId(), authUser.getUserId());
        if (userWorkspace == null || userWorkspace.getWorkSpaceUserRole() == WorkSpaceUserRole.ROLE_READ_USER) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }
        //카드가 존재하는 지 확인
        Card card=cardRepository.findById(cardId).orElseThrow(()->
                new NotFoundException(NOT_FOUND_CARD));

        //새로운 댓글 생성 및 저장
        Comment newComment=Comment.from(saveCommentRequestDto,user,card);
        commentRepository.save(newComment);

        /**
         * 댓글 저장 알림 전송 이벤트 발생
         */
        eventPublisher.publishEvent(new SavedCommentEvent(authUser.getUserId(),card.getId()));

        return SaveCommentResponseDto.from(newComment);
    }

    //댓글 수정
    @Transactional
    public UpdateCommentResponseDto updateComment(AuthUser authUser, Long commentId, @Valid UpdateCommentRequestDto updateCommentRequest) {
        //댓글이 존재하는 지 확인
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->
                new NotFoundException(NOT_FOUND_COMMENT));

        //댓글을 작성한 유저가 맞는 지 확인
        if(!authUser.getUserId().equals(comment.getUser().getId())){
            throw new UnauthorizedException(INVALID_USER_AUTHORITY);
        }
        comment.update(updateCommentRequest);


        return UpdateCommentResponseDto.from(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(AuthUser authUser, Long commentId) {
        //댓글이 존재하는 지 확인
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->
                new NotFoundException(NOT_FOUND_COMMENT));

        //댓글을 작성한 유저가 맞는 지 확인
        if(!authUser.getUserId().equals(comment.getUser().getId())){
            throw new UnauthorizedException(INVALID_USER_AUTHORITY);
        }

        commentRepository.deleteById(commentId);
    }


}
