package com.example.demo_web.model.service;

import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.MovieReview;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovieReviewService {
    Optional<String> create(String reviewTitle, String reviewBody, String stringMovieId, String stringUserId) throws ServiceException;
    Optional<String> update(String stringMovieReviewId, String reviewTitle, String reviewBody, String stringMovieId, String stringUserId) throws ServiceException;
    Optional<String> delete(String stringReviewId, String stringMovieId, String stringUserId) throws ServiceException;
    Map.Entry<Optional<MovieReview>, Optional<String>> findById(String stringMovieReviewId, String stringMovieId, String stringUserId) throws ServiceException;
    Map.Entry<List<String>, List<String>> validateData(String title, String body, String stringMovieId, String stringUserId);
}
