package com.sparta.trelloproject.domain.card.controller;

import com.sparta.trelloproject.common.response.SuccessResponse;
import com.sparta.trelloproject.domain.card.dto.request.ManagerRequestDto;
import com.sparta.trelloproject.domain.card.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ManagerController {

    private final ManagerService managerService;

    /**
     * 매니저를 등록합니다
     *
     * @param managerRequestDto userid와 cardid를 받습니다
     * @return
     */
    @PostMapping("/manager")
    public ResponseEntity<SuccessResponse<String>> saveCard(
        @RequestBody ManagerRequestDto managerRequestDto) {
        managerService.addManager(managerRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }
}
