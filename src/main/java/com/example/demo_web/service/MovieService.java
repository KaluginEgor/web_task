package com.example.demo_web.service;

import com.example.demo_web.entity.Movie;
import com.example.demo_web.exception.ServiceException;

import java.util.List;

public interface MovieService {
    List<Movie> findAll(int begin, int end) throws ServiceException;

    int countMovies() throws ServiceException;

    Movie findById(Integer id) throws ServiceException;
}