package com.goottflix.movie.controller;

import com.goottflix.movie.model.Member;
import com.goottflix.movie.service.MovieService;
import com.goottflix.review.model.Review;
import com.goottflix.movie.model.Movie;
import com.goottflix.review.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    private final ReviewService reviewService;

    @GetMapping("/movie/write")
    public String write() {
        return "movieWrite";
    }

    @PostMapping("/movie/write")
    public String writePost(Movie movie, @RequestParam("file") MultipartFile file) throws IOException {
        movieService.save(movie, file);
        return "redirect:/movie/list";
    }

    @GetMapping("/movie/list")
    public String List(Model model, HttpSession session) {
        List<Movie> list = movieService.getAllMovies();

        model.addAttribute("list", list);

        if(session.getAttribute("member") != null) {
            Member member = (Member) session.getAttribute("member");
            List<Movie> recommendList = movieService.getRecommendedMovies(member.getId());
            model.addAttribute("recommendList",recommendList);
        }

        return "movieList";
    }

    @GetMapping("/movie/view")
    public String views(Model model, @RequestParam("id") Long id) {
        List<Review> reviews = reviewService.getReviewByMovieId(id);
        Movie movie = movieService.getMovieById(id);

        String[] genre = movie.getGenre().split("_");

        model.addAttribute("reviews", reviews);
        model.addAttribute("movie", movie);
        model.addAttribute("genre", genre);

        return "movieView";
    }

    @GetMapping("/movie/review")
    public String reviews(@RequestParam("id") Long movieId, Model model){

        model.addAttribute("movieId", movieId);

        return "movieReview";
    }

    @PostMapping("/movie/review")
    public String reviewPost(@RequestParam("movieId") Long movieId,
                             @RequestParam("userId") Long userId,
                             @RequestParam("rating") int rating,
                             @RequestParam(name = "review",required = false) String review){

        Review review1 = new Review();

        review1.setMovieId(movieId);
        review1.setUserId(userId);
        review1.setRating(rating);
        if(review != null) {
            review1.setReview(review);
        }

        reviewService.save(review1);

        float avg = reviewService.getAverageRatingByMovieId(movieId);
        movieService.updateRating(avg,movieId);

        return "redirect:/movie/list";
    }

    @PostMapping("/movie/recommendUp")
    public String recommendUp(@RequestParam("reviewId") Long reviewId,@RequestParam("movieId") Long movieId){
        //reviewService.recommendUp(reviewId);
        return "redirect:/movie/view?id="+movieId;
    }
}
