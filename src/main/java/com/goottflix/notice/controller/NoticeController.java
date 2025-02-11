package com.goottflix.notice.controller;

import com.goottflix.notice.model.Notice;
import com.goottflix.notice.service.NoticeService;
import com.goottflix.user.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class NoticeController {

    private final NoticeService noticeService;
    private final JWTUtil jwtUtil;

    @GetMapping("/notice/list")
    public List<Notice> getNotices() {
        return noticeService.getAllNotices();
    }

    @PostMapping("/notice/create")
    public ResponseEntity<String> createNotice(@CookieValue("Authorization") String token,
                                               @RequestBody Notice notice) {
        Long userId = jwtUtil.getUserID(token);

    }

}
