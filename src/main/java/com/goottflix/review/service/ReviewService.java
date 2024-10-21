package com.goottflix.review.service;

import com.goottflix.review.model.Likes;
import com.goottflix.review.model.Review;
import com.goottflix.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;

    public List<Review> getReviewByMovieId(Long movieId){
        return reviewMapper.findByMovieId(movieId);
    }

    public Review getReviewByUserIdAndMovieId(Map<String, Object> params){
        return reviewMapper.findByUserIdAndMovieId(params);
    }

    public void save(Review review){
        if(review.getId()==null){
            reviewMapper.save(review);
        }else{
            reviewMapper.update(review);
        }
    }

    public void delete(Long id){
        reviewMapper.delete(id);
    }

    public float getAverageRatingByMovieId(Long movieId){
        List<Review> reviews = getReviewByMovieId(movieId);
        float averageRating = 0;
        for(Review review : reviews){
            averageRating+=review.getRating();
        }
        return averageRating/reviews.size();
    }

    public void recommendUp(Long reviewId, Long userId){
        Review review = reviewMapper.findById(reviewId);
        Map<String, Object> params = new HashMap<>();

        if(isLiked(reviewId, userId)){
            review.setRecommend(review.getRecommend()-1);
            params.put("reviewId", reviewId);
            params.put("userId", userId);
            reviewMapper.likesDelete(params);
        }else{
            review.setRecommend(review.getRecommend()+1);
            params.put("reviewId", reviewId);
            params.put("userId", userId);
            reviewMapper.likesSave(params);
        }
        reviewMapper.update(review);
    }

    public boolean isLiked(Long reviewId, Long userId){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("reviewId", reviewId);
        Likes likes = reviewMapper.findByReviewIdAndUserId(params);
        if(likes==null){
            return false;
        }else{
            return true;
        }
    }

    public void declaration(Long reviewId){
        Review review = reviewMapper.findById(reviewId);
        Map<String, Object> params = new HashMap<>();
        params.put("id", reviewId);
        params.put("spoiler",(review.getSpoiler()+1));
        reviewMapper.declaration(params);
    }

    public List<Review> getReviewBySpoiler(){
        return reviewMapper.findBySpoiler();
    }
}
