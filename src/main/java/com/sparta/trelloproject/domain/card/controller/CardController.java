package com.sparta.trelloproject.domain.card.controller;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.response.SuccessResponse;
import com.sparta.trelloproject.domain.card.dto.reponse.CardListResponseDto;
import com.sparta.trelloproject.domain.card.dto.reponse.CardResponseDto;
import com.sparta.trelloproject.domain.card.dto.request.CardAuthRequestDto;
import com.sparta.trelloproject.domain.card.dto.request.CardRequestDto;
import com.sparta.trelloproject.domain.card.dto.request.CardStatusRequestDto;
import com.sparta.trelloproject.domain.card.service.CardService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    /**
     * 카드를 생성합니다
     *
     * @param file           파일을 입력받습니다
     * @param cardRequestDto 카드를 등록할 타이틀, 컨텐츠, 마감일과 등록할 list의 id, 워크스페이스 접근 권한을 알기위한 workSpaceid를
     *                       받습니다
     * @param authUser       현재 로그인중인 유저 정보를 가져오기 위함입니다.
     * @return 업로드한 파일 이름이 반환됩니다.
     */
    @PostMapping("/cards")
    public ResponseEntity<SuccessResponse<String>> saveCard(
        @RequestParam("file") MultipartFile file,
        @ModelAttribute CardRequestDto cardRequestDto,
        @AuthenticationPrincipal AuthUser authUser) {
        String uploadedFileName = cardService.saveCard(authUser.getUserId(), file, cardRequestDto);

        return ResponseEntity.ok(SuccessResponse.of(uploadedFileName));
    }

    /**
     * 카드를 수정합니다
     *
     * @param file           파일을 입력받습니다
     * @param cardRequestDto 카드를 등록할 타이틀, 컨텐츠, 마감일과 등록할 list의 id, 워크스페이스 접근 권한을 알기위한 workSpaceid를
     *                       받습니다
     * @param cardId         수정할 카드 id를 PathVariable 입력받습니다.
     * @param authUser       현재 로그인중인 유저 정보를 가져오기 위함입니다.
     * @return 수정된 파일 이름을 반환합니다.
     */
    @PatchMapping("/cards/{cardId}")
    public ResponseEntity<SuccessResponse<String>> updateCard(
        @RequestParam("file") MultipartFile file,
        @ModelAttribute CardRequestDto cardRequestDto, @PathVariable Long cardId,
        @AuthenticationPrincipal AuthUser authUser) {
        String uploadedFileName = cardService.updateCard(file, cardRequestDto, cardId,
            authUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.of(uploadedFileName));
    }

    /**
     * 카드를 다건 조회합니다, 제목, 내용, 매니저이름, 기한을 검색하며 페이지네이션을 구현했습니다.
     *
     * @param title       제목
     * @param contents    내용
     * @param manager     매니저 이름
     * @param dueDateFrom 기한시작
     * @param dueDateTo   기한 끝
     * @param page        페이지
     * @param size        페이지
     * @return
     */
    @GetMapping("/cards")
    public ResponseEntity<SuccessResponse<Page<CardListResponseDto>>> getCardList(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String contents,
        @RequestParam(required = false) String manager,
        @RequestParam(name = "due-date-from", required = false) LocalDateTime dueDateFrom,
        @RequestParam(name = "due-date-to", required = false) LocalDateTime dueDateTo,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(SuccessResponse.of(
            cardService.getCardList(title, contents, manager, dueDateFrom, dueDateTo, page, size)));
    }

    /**
     * 카드를 상세 조회합니다.
     *
     * @param id card의 id를 검색합니다.
     * @return 카드의 내용과, 카드의 댓글, 카드의 이미지를 반환합니다.
     */
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<SuccessResponse<CardResponseDto>> getCard(@PathVariable Long cardId,
        @AuthenticationPrincipal AuthUser authUser,
        @RequestBody CardAuthRequestDto cardAuthRequestDto) {
        return ResponseEntity.ok(SuccessResponse.of(
            cardService.getCard(cardId, authUser.getUserId(), cardAuthRequestDto)));
    }

    /**
     * 카드를 삭제합니다
     *
     * @param id 카드 id를 입력받습니다
     * @return
     */
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<SuccessResponse<String>> deleteCard(@PathVariable Long cardId,
        @RequestBody CardAuthRequestDto cardAuthRequestDto,
        @AuthenticationPrincipal AuthUser authUser) {
        cardService.deleteCard(cardAuthRequestDto, cardId, authUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

    /**
     * 카드 상태를 업데이트 합니다
     *
     * @param cardId 카드 아이디를 입력받습니다.
     * @return
     */
    @PatchMapping("/cards/{cardId}/status")
    public ResponseEntity<SuccessResponse<String>> updateCardStatus(@PathVariable Long cardId,
        @RequestBody CardStatusRequestDto cardStatusRequestDto,
        @AuthenticationPrincipal AuthUser authUser) {
        cardService.updateCardStatus(cardId, authUser.getUserId(), cardStatusRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

    /**
     * 카드의 이미지를 삭제합니다.
     *
     * @param cardId 카드 아이디를 받아 이미지를 찾아서 삭제합니다
     * @return 반환타입은
     */
    @DeleteMapping("/cards/{cardId}/images")
    public ResponseEntity<SuccessResponse<String>> deleteCardImage(@PathVariable Long cardId,
        @RequestBody CardAuthRequestDto cardAuthRequestDto,
        @AuthenticationPrincipal AuthUser authUser) {
        cardService.deleteCardImage(cardId, authUser.getUserId(), cardAuthRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

}
