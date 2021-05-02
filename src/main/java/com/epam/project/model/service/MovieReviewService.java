package com.epam.project.model.service;

import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.MovieReview;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface Movie review service.
 */
public interface MovieReviewService {
    /**
     * Create optional.
     *
     * @param reviewTitle   the review title
     * @param reviewBody    the review body
     * @param stringMovieId the string movie id
     * @param stringUserId  the string user id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> create(String reviewTitle, String reviewBody, String stringMovieId, String stringUserId) throws ServiceException;

    /**
     * Update optional.
     *
     * @param stringMovieReviewId the string movie review id
     * @param reviewTitle         the review title
     * @param reviewBody          the review body
     * @param stringMovieId       the string movie id
     * @param stringUserId        the string user id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> update(String stringMovieReviewId, String reviewTitle, String reviewBody, String stringMovieId, String stringUserId) throws ServiceException;

    /**
     * Delete optional.
     *
     * @param stringReviewId the string review id
     * @param stringMovieId  the string movie id
     * @param stringUserId   the string user id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> delete(String stringReviewId, String stringMovieId, String stringUserId) throws ServiceException;

    /**
     * Find by id map . entry.
     *
     * @param stringMovieReviewId the string movie review id
     * @param stringMovieId       the string movie id
     * @param stringUserId        the string user id
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<MovieReview>, Optional<String>> findById(String stringMovieReviewId, String stringMovieId, String stringUserId) throws ServiceException;

    /**
     * Validate data map . entry.
     *
     * @param title         the title
     * @param body          the body
     * @param stringMovieId the string movie id
     * @param stringUserId  the string user id
     * @return the map . entry
     */
    Map.Entry<List<String>, List<String>> validateData(String title, String body, String stringMovieId, String stringUserId);
}
