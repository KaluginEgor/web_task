package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.GenreType;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.DaoException;

import java.util.List;
import java.util.Map;

public interface MovieDao extends BaseDao<Integer, Movie> {
    List<Movie> findAllBetween(int begin, int end) throws DaoException;
    int countMovies() throws DaoException;
    List<GenreType> findGenreTypesByMovieId(Integer id) throws DaoException;
    List<Movie> findByActorId(Integer id) throws DaoException;
    Map<Integer, String> findAllTitles() throws DaoException;
}
