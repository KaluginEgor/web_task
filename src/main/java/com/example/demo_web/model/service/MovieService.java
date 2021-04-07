package com.example.demo_web.model.service;

import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface MovieService {
    List<Movie> findAllBetween(int begin, int end) throws ServiceException;

    int countMovies() throws ServiceException;

    Movie findById(Integer id) throws ServiceException;

    Map<Integer, String> findAllTitles() throws ServiceException;
}