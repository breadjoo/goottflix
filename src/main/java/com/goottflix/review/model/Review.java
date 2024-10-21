package com.goottflix.review.model;

import lombok.Data;

@Data
public class Review {
    private Long id;
    private Long movieId;
    private Long userId;
    private int rating;
    private String review;
    private int recommend;
    private int spoiler;
}
