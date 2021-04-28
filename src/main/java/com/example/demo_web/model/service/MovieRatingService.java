package com.example.demo_web.model.service;

import com.example.demo_web.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface MovieRatingService {

    Optional<String> create(String stringMovieId, String stringUserId, String stringValue) throws ServiceException;
    Optional<String> update(String stringRatingId, String stringMovieId, String stringUserId, String stringValue) throws ServiceException;
    Optional<String> delete(String stringRatingId, String stringMovieId, String stringUserId) throws ServiceException;
    List<String> validateData(String stringMovieId, String stringUserId, String stringValue);
}
