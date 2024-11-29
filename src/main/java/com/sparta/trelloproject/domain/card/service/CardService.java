package com.sparta.trelloproject.domain.card.service;

import static com.sparta.trelloproject.common.exception.ResponseCode.FORBIDDEN;
import static com.sparta.trelloproject.common.exception.ResponseCode.NOT_FOUND_CARD;

import com.sparta.trelloproject.common.annotation.RedissonLock;
import com.sparta.trelloproject.common.exception.ForbiddenException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.common.s3.S3Service;
import com.sparta.trelloproject.domain.card.dto.CardAuthorityDto;
import com.sparta.trelloproject.domain.card.dto.reponse.CardImageResponseDto;
import com.sparta.trelloproject.domain.card.dto.reponse.CardListResponseDto;
import com.sparta.trelloproject.domain.card.dto.reponse.CardResponseDto;
import com.sparta.trelloproject.domain.card.dto.request.CardAuthRequestDto;
import com.sparta.trelloproject.domain.card.dto.request.CardImageRequestDto;
import com.sparta.trelloproject.domain.card.dto.request.CardRequestDto;
import com.sparta.trelloproject.domain.card.dto.request.CardStatusRequestDto;
import com.sparta.trelloproject.domain.card.entity.Card;
import com.sparta.trelloproject.domain.card.entity.CardImage;
import com.sparta.trelloproject.domain.card.repository.CardImageRepository;
import com.sparta.trelloproject.domain.card.repository.CardRepository;
import com.sparta.trelloproject.domain.comment.dto.response.UpdateCommentResponseDto;
import com.sparta.trelloproject.domain.comment.repository.CommentRepository;
import com.sparta.trelloproject.domain.list.dto.ListAuthorityDto;
import com.sparta.trelloproject.domain.list.entity.Lists;
import com.sparta.trelloproject.domain.list.repository.ListRepository;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final S3Service s3Service;
    private final UserWorkSpaceRepository userWorkSpaceRepository;
    private final CardImageRepository cardImageRepository;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public String saveCard(long userId, MultipartFile file, CardRequestDto cardRequestDto) {
        ListAuthorityDto listAuthorityDto = userWorkSpaceRepository.findByListId(
            cardRequestDto.getWorkSpaceId(), userId, cardRequestDto.getListId());

        if (listAuthorityDto.getWorkspaceAuthority().getSeq()
            > WorkSpaceUserRole.ROLE_EDIT_USER.getSeq()) {
            throw new ForbiddenException(FORBIDDEN);
        }

        Lists lists = listRepository.findByListId(cardRequestDto.getListId());

        Card card = Card.from(cardRequestDto, lists);
        cardRepository.save(card);

        String uploadImageUrl = s3Service.uploadFile(file);

        CardImageRequestDto cardImageDto = createCardImageDto(file, uploadImageUrl);
        cardImageRepository.save(CardImage.of(cardImageDto, card));

        return uploadImageUrl;
    }

    public String updateCard(MultipartFile file, CardRequestDto cardRequestDto, Long cardId,
        long userId) {

        validateUserAuthority(cardRequestDto.getWorkSpaceId(), userId, cardId);

        Card card = cardRepository.findByCardId(cardId);
        CardImage cardImage = cardImageRepository.findByCardId(cardId);
        String existingFilePath = cardImage != null ? cardImage.getPath() : null;

        card.update(cardRequestDto);

        String uploadImageUrl = s3Service.updateFile(file, existingFilePath);

        CardImageRequestDto cardImageDto = createCardImageDto(file, uploadImageUrl);

        /**
         * 카드 수정 알림 전송 이벤트 발생
         */
//        eventPublisher.publishEvent(new UpdatedCardEvent(userId, cardId));
        if (cardImage != null) {
            cardImage.update(cardImageDto);
        } else {
            cardImageRepository.save(CardImage.of(cardImageDto, card));
        }
        return uploadImageUrl;
    }

    @Transactional(readOnly = true)
    public Page<CardListResponseDto> getCardList(String title, String contents, String manager,
        LocalDateTime dueDateFrom,
        LocalDateTime dueDateTo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cardRepository.findCardList(title, contents, manager, dueDateFrom, dueDateTo,
            pageable);
    }

    @Transactional(readOnly = true)
    public CardResponseDto getCard(Long cardId, Long userId,
        CardAuthRequestDto cardAuthRequestDto) {
        validateUserAuthority(cardAuthRequestDto.getWorkSpaceId(), userId, cardId);

        Card card = cardRepository.findByCardId(cardId);

        List<UpdateCommentResponseDto> comments = commentRepository.findByCardId(cardId).stream()
            .map(UpdateCommentResponseDto::from)
            .collect(Collectors.toList());

        CardImage cardImage = cardImageRepository.findByCardId(cardId);

        if (cardImage != null) {
            CardImageResponseDto cardImageResponse = CardImageResponseDto.from(cardImage);
            return CardResponseDto.of(card, comments, cardImageResponse);
        }

        return CardResponseDto.of(card, comments);
    }

    public void deleteCard(CardAuthRequestDto cardAuthRequestDto, Long cardId, Long userId) {
        validateUserAuthority(cardAuthRequestDto.getWorkSpaceId(), userId, cardId);

        Card card = cardRepository.findByCardId(cardId);
        cardRepository.delete(card);
    }

    @RedissonLock("#update-card")
    public void updateCardStatus(Long cardId, Long userId,
        CardStatusRequestDto cardStatusRequestDto) {
        validateUserAuthority(cardStatusRequestDto.getWorkSpaceId(), userId, cardId);
        Card card = cardRepository.findByCardId(cardId);
        card.updateStatus(cardStatusRequestDto);
    }

    public void deleteCardImage(Long cardId, Long userId, CardAuthRequestDto cardAuthRequestDto) {
        validateUserAuthority(cardAuthRequestDto.getWorkSpaceId(), userId, cardId);
        CardImage cardImage = cardImageRepository.findByCardId(cardId);
        if (cardImage == null) {
            throw new NotFoundException(NOT_FOUND_CARD);
        }
        s3Service.deleteFile(cardImage.getFileName());
        cardImageRepository.delete(cardImage);
    }

    private CardImageRequestDto createCardImageDto(MultipartFile multipartFile, String url) {
        String originalFileName = multipartFile.getOriginalFilename();
        String originName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        return new CardImageRequestDto(url, fileName, originName, extension);
    }

    private CardAuthorityDto validateUserAuthority(long workspaceId, long userId, long cardId) {
        CardAuthorityDto cardAuthorityDto = userWorkSpaceRepository.findByCardId(workspaceId,
            userId, cardId);

        if (cardAuthorityDto.getWorkspaceAuthority().getSeq()
            > WorkSpaceUserRole.ROLE_EDIT_USER.getSeq()) {
            throw new ForbiddenException(FORBIDDEN);
        }

        return cardAuthorityDto;
    }

}
