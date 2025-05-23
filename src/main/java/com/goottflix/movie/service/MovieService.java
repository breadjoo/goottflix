package com.goottflix.movie.service;

import com.goottflix.common.FileService;
import com.goottflix.movie.mapper.MovieMapper;
import com.goottflix.movie.model.Movie;
import com.goottflix.review.mapper.ReviewMapper;
import com.goottflix.review.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieMapper movieMapper;
    private final ReviewMapper reviewMapper;
    private final FileService fileService;


    public void save(Movie movie, MultipartFile file) throws IOException {

        System.out.println(movie.getTitle());
        System.out.println(file.getOriginalFilename());
        if(!file.isEmpty()){
            movie.setPosterUrl(fileService.saveFile(file));
        }
        if(movie.getId()==null){
            movieMapper.save(movie);
        }else{
            fileService.deleteExistingFile(movie.getPosterUrl());
            movieMapper.update(movie);
        }
    }

    public void modify (Movie movie, MultipartFile file) throws IOException {
        if (movie.getId() == null) {
            throw new IllegalArgumentException("영화 ID가 필요합니다.");
        }
        if(!file.isEmpty()){
            fileService.deleteExistingFile(movie.getPosterUrl());
            movie.setPosterUrl(fileService.saveFile(file));
        }
        System.out.println("modify 서비스입니다 movie.getIntro() = " + movie.getIntro());
        movieMapper.update(movie);
    }

    public void updateRating(float avg, Long movieId){
        Movie movie1 = movieMapper.findById(movieId);
        movie1.setRating(avg);
        movieMapper.update(movie1);
    }


    public List<Movie> getAllMovies() {
        return movieMapper.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieMapper.findById(id);
    }

    public List<Movie> getRecommendedMovies(Long userId){
        List<Review> reviews = reviewMapper.findByRecommendedUserId(userId);

//        0 = 코미디
//        1 = 액션
//        2 = SF
//        3 = 어린이
//        4 = 공포
//        5 = 모험
        int[] genreCount = new int[6];   //인트 배열 크기는 장르의 갯수 만큼
        String[] genres = {"코미디", "액션", "SF", "어린이", "공포", "모험"};

        for(int i=0;i<reviews.size();i++){
            if(reviews.get(i).getRating()>=4){
                Movie movie = movieMapper.findById(reviews.get(i).getMovieId());
                if(movie.getGenre().contains(genres[0])){
                    genreCount[0]++;
                }else if(movie.getGenre().contains(genres[1])){
                    genreCount[1]++;
                }else if(movie.getGenre().contains(genres[2])){
                    genreCount[2]++;
                }else if(movie.getGenre().contains(genres[3])){
                    genreCount[3]++;
                }else if(movie.getGenre().contains(genres[4])){
                    genreCount[4]++;
                }else if(movie.getGenre().contains(genres[5])){
                    genreCount[5]++;
                }
            }
        }

        int max = 0;
        String maxGenre = "";

        for(int i=0;i<genres.length;i++){
            if(max<genreCount[i]){
                maxGenre = genres[i];
            }
        }

        return movieMapper.findByRecommendedGenre("%"+maxGenre+"%");
    }

    public void delete(Long id){
        movieMapper.delete(id);
    }
    public List<Movie> getMoviesWithPage(int page, int size) {
        int offset = (page - 1) * size;
        return movieMapper.getMoviesWithPage(size, offset);
    }

    public int getTotalMovieCount() {
        return movieMapper.getTotalMovieCount();
    }

    public List<Movie> getFilteredMovies(String genre, String nation, String director, String sortBy) {
        return movieMapper.getFilteredMovies(genre, nation, director, sortBy);
    }
}
