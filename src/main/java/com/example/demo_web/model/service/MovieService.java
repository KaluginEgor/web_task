package com.example.demo_web.model.service;

import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    List<Movie> findAllBetween(int begin, int end) throws ServiceException;

    int countMovies() throws ServiceException;

    Movie findById(Integer id) throws ServiceException;

    List<Movie> findAll() throws ServiceException;

    Movie create(String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws ServiceException;

    Movie update(int id, String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws ServiceException;

    boolean delete(int id) throws ServiceException;

    List<Movie> findByNamePart(String title) throws ServiceException;
}