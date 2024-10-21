package com.goottflix.review.mapper;

import com.goottflix.review.model.Likes;
import com.goottflix.review.model.Review;
import com.goottflix.review.model.ReviewRank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper {

    List<Review> findByMovieId(Long movieId);

    Review findById(Long id);

    Review findByUserIdAndMovieId(Map<String, Object> params);

    void save(Review review);

    void update(Review review);

    void delete(Long id);

    List<Review> findByRecommendedUserId(Long userId);

    void likesSave(Map<String, Object> params);

    Likes findByReviewIdAndUserId(Map<String, Object> params);

    void likesDelete(Map<String, Object> params);

    List<Review> findBySpoiler();

    void declaration(Map<String, Object> params);

    void initializeRank();
    int countTotalUsers();

    List<ReviewRank> getRankedReviews();


}
