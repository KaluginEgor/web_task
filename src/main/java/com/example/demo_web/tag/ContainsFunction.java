package com.example.demo_web.tag;

import com.example.demo_web.model.entity.GenreType;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.MovieRating;

import java.util.List;

public class ContainsFunction {
    public static MovieRating getUserRate(List<MovieRating> ratingList, int userId) {
        MovieRating useRate = null;
        for (MovieRating movieRating : ratingList) {
            if (movieRating.getUserId() == userId) {
                useRate = movieRating;
                break;
            }
        }
        return useRate;
    }

    public static boolean containsGenreType(List<GenreType> genreTypes, GenreType genreType) {
        for (GenreType element : genreTypes) {
            if (genreType.equals(element)) {
                return true;
            }
        }
        return false;
    }
}
