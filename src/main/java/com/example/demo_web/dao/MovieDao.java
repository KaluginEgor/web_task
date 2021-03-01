package com.example.demo_web.dao;

import com.example.demo_web.entity.Movie;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public interface MovieDao extends BaseDao<Integer, Movie> {
    List<Movie> findAllBetween(int begin, int end) throws DaoException;
    int countMovies() throws DaoException;
}
