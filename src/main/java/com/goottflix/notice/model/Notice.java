package com.goottflix.notice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice {

    private Long id;
    private String title;
    private String content;
    private String image;
    private Long writer;
    private String password;
    private LocalDateTime createdAt;
}
