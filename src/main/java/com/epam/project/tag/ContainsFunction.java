package com.epam.project.tag;

import com.epam.project.model.entity.GenreType;
import com.epam.project.model.entity.MediaPerson;
import com.epam.project.model.entity.Movie;
import com.epam.project.model.entity.MovieRating;

import java.util.List;

/**
 * The type Contains function.
 */
public class ContainsFunction {
    /**
     * Gets user rate.
     *
     * @param ratingList the rating list
     * @param userId     the user id
     * @return the user rate
     */
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

    /**
     * Contains genre type boolean.
     *
     * @param genreTypes the genre types
     * @param genreType  the genre type
     * @return the boolean
     */
    public static boolean containsGenreType(List<GenreType> genreTypes, GenreType genreType) {
        for (GenreType element : genreTypes) {
            if (genreType.equals(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contains movie id boolean.
     *
     * @param movies the movies
     * @param id     the id
     * @return the boolean
     */
    public static boolean containsMovieId(List<Movie> movies, Integer id) {
        for (Movie movie : movies) {
            if (id.equals(movie.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contains media person id boolean.
     *
     * @param mediaPersons the media persons
     * @param id           the id
     * @return the boolean
     */
    public static boolean containsMediaPersonId(List<MediaPerson> mediaPersons, Integer id) {
        for (MediaPerson mediaPerson : mediaPersons) {
            if (id.equals(mediaPerson.getId())) {
                return true;
            }
        }
        return false;
    }
}
