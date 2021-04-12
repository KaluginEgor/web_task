package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.GenreType;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public interface MovieDao extends BaseDao<Integer, Movie> {
    List<Movie> findAllBetween(int begin, int end) throws DaoException;
    int countMovies() throws DaoException;
    List<GenreType> findGenreTypesByMovieId(Integer id) throws DaoException;
    List<Movie> findByActorId(Integer id) throws DaoException;
    boolean insertMovieGenre(Integer movieId, Integer genreId) throws DaoException;
    boolean insertMovieMediaPerson(Integer movieId, Integer mediaPersonId) throws DaoException;
    boolean deleteMovieCrew(Integer movieId) throws DaoException;
    boolean deleteMovieGenres(Integer movieId) throws DaoException;
}
