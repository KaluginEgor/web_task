package com.example.demo_web.model.service;

import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovieService {
    List<Movie> findAllBetween(int begin, int end) throws ServiceException;

    int countMovies() throws ServiceException;

    Map.Entry<Optional<Movie>, Optional<String>> findById(String stringMovieId) throws ServiceException;

    List<Movie> findAll() throws ServiceException;

    Map.Entry<Optional<Movie>, Optional<String>> create(String title, String description, String stringReleaseDate,
                                                        String picture, String[] stringGenres,
                                                        String[] stringMediaPersonsId) throws ServiceException;

    Map.Entry<Optional<Movie>, Optional<String>> update(String stringId, String title, String description, String releaseDate,
                                                        String picture, String[] stringGenres,
                                                        String[] stringMediaPersonsId) throws ServiceException;

    Optional<String> delete(String stringId) throws ServiceException;

    Map.Entry<List<Movie>, Optional<String>> findByNamePart(String title) throws ServiceException;

    Map.Entry<List<String>, List<String>> validateData(String title, String description, String stringReleaseDate,
                                                       String[] stringGenresId, String[] stringMediaPersonsId);
}