package com.epam.project.model.util.evaluator;

public class UserRatingEvaluator {
    private UserRatingEvaluator() {}

    public static int updateRating(int userRating, float movieRatingDifference) {
        if (movieRatingDifference <= 1) {
            userRating += 5;
        } else if (movieRatingDifference > 1 && movieRatingDifference <= 2) {
            userRating += 4;
        } else if (movieRatingDifference > 2 && movieRatingDifference <= 3) {
            userRating += 3;
        } else if (movieRatingDifference > 3 && movieRatingDifference <= 4) {
            userRating += 2;
        } else if (movieRatingDifference > 4 && movieRatingDifference <= 5) {
            userRating += 1;
        } else if (movieRatingDifference > 5 && movieRatingDifference <= 6) {
            userRating -= 1;
        } else if (movieRatingDifference > 6 && movieRatingDifference <= 7) {
            userRating -= 2;
        } else if (movieRatingDifference > 7 && movieRatingDifference <= 8) {
            userRating -= 3;
        } else if (movieRatingDifference > 8 && movieRatingDifference <= 9) {
            userRating -= 4;
        } else if (movieRatingDifference > 9 && movieRatingDifference <= 10) {
            userRating -= 5;
        }
        return userRating;
    }
}
