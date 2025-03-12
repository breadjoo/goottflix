package com.goottflix.notice.controller;

import com.goottflix.notice.model.Notice;
import com.goottflix.notice.service.NoticeService;
import com.goottflix.user.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NoticeController {

    private final NoticeService noticeService;
    private final JWTUtil jwtUtil;

    @GetMapping("/notice/list")
    public List<Notice> getNotices() {
        System.out.println("getAllNotice실행");
        return noticeService.getAllNotices();
    }

    @PostMapping(value = "/notice/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createNotice(@CookieValue("Authorization") String token,
                                          @RequestPart("notice") Notice notice,
                                          @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        System.out.println("공지사항 작성");
        System.out.println("notice.getTitle() = " + notice.getTitle());
        Long writer = jwtUtil.getUserID(token);
        noticeService.save(writer, notice, file);
        return ResponseEntity.ok().build();
    }
}
