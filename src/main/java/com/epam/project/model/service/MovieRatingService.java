package com.epam.project.model.service;

import com.epam.project.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface MovieRatingService {

    Optional<String> create(String stringMovieId, String stringUserId, String stringValue) throws ServiceException;
    Optional<String> update(String stringRatingId, String stringMovieId, String stringUserId, String stringValue) throws ServiceException;
    Optional<String> delete(String stringRatingId, String stringMovieId, String stringUserId) throws ServiceException;
    List<String> validateData(String stringMovieId, String stringUserId, String stringValue);
}
