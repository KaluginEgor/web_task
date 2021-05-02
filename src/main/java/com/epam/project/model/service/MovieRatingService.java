package com.epam.project.model.service;

import com.epam.project.exception.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * The interface Movie rating service.
 */
public interface MovieRatingService {

    /**
     * Create optional.
     *
     * @param stringMovieId the string movie id
     * @param stringUserId  the string user id
     * @param stringValue   the string value
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> create(String stringMovieId, String stringUserId, String stringValue) throws ServiceException;

    /**
     * Update optional.
     *
     * @param stringRatingId the string rating id
     * @param stringMovieId  the string movie id
     * @param stringUserId   the string user id
     * @param stringValue    the string value
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> update(String stringRatingId, String stringMovieId, String stringUserId, String stringValue) throws ServiceException;

    /**
     * Delete optional.
     *
     * @param stringRatingId the string rating id
     * @param stringMovieId  the string movie id
     * @param stringUserId   the string user id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> delete(String stringRatingId, String stringMovieId, String stringUserId) throws ServiceException;

    /**
     * Validate data list.
     *
     * @param stringMovieId the string movie id
     * @param stringUserId  the string user id
     * @param stringValue   the string value
     * @return the list
     */
    List<String> validateData(String stringMovieId, String stringUserId, String stringValue);
}
