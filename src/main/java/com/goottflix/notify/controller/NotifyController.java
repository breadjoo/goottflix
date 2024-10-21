package com.goottflix.notify.controller;


import com.goottflix.notify.entity.NotifyEntity;
import com.goottflix.notify.service.NotifyService;
import com.goottflix.user.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class NotifyController {

    private final NotifyService notifyService;
    private final JWTUtil jwtUtil;

    // 영화 업데이트
    @PostMapping("/movieupdate")
    public ResponseEntity<?> addMovieUpdate(@RequestBody NotifyEntity notifyEntity) {
        try {
            notifyService.addMovieUpdate(notifyEntity.getMovieId());
            return ResponseEntity.ok("업데이트 알림 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업데이트에 실패했습니다");
        }
    }

    // 친구 추가 업데이트
    @PostMapping("/friendUpdate")
    public ResponseEntity<?> addFriendUpdate(@RequestParam("userId") Long userId) {
        try {
            notifyService.addFriendUpdate(userId);
            return ResponseEntity.ok("친구 추가 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 추가 실패");
        }
    }


    // sse 구현
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@CookieValue("Authorization") String token) {
        System.out.println("sse작동중");
        Long userId = jwtUtil.getUserID(token);
        return notifyService.subscribe(userId);
    }

    // 알림 읽음 확인
    @PutMapping("/read")
    public ResponseEntity<?> readNotify(@RequestParam("userId") Long userId, @RequestParam("notifyId") Long notifyId) {
        try {
            // 서비스 계층에서 알림 읽음 처리
            notifyService.notifyRead(userId, notifyId);
            return ResponseEntity.ok("알림을 읽음");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알림 읽음 처리 실패");
        }
    }
    // 알림 전체 조회
    @GetMapping("/allnotify")
    public ResponseEntity<List<NotifyEntity>> getAllNotify(@CookieValue("Authorization") String token) {
        System.out.println("jwtutil = " + jwtUtil.getUserID(token));

            List<NotifyEntity> notifyEntities = notifyService.getAllNotify(jwtUtil.getUserID(token));
            System.out.println("notifyEntities = " + notifyEntities.size());
            return ResponseEntity.ok(notifyEntities);
    }

    // 알림 삭제
    @DeleteMapping("/deleteNotify")
    public ResponseEntity<?> deleteNotify(@RequestParam("notifyId") Long notifyId) {
        try {
            notifyService.deleteNotify(notifyId);
            return ResponseEntity.ok("알림 삭제 성공");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("실패");
        }
    }



}