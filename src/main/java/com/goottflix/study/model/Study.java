package com.goottflix.study.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Study {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime created;

}

