package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.GenreType;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public abstract class AbstractMovieDao extends AbstractBaseDao<Integer, Movie> {
    public abstract List<Movie> findAllBetween(int begin, int end) throws DaoException;
    public abstract int countMovies() throws DaoException;
    public abstract List<GenreType> findGenreTypesByMovieId(Integer id) throws DaoException;
    public abstract List<Movie> findByActorId(Integer id) throws DaoException;
    public abstract boolean insertMovieGenre(Integer movieId, Integer genreId) throws DaoException;
    public abstract boolean insertMovieMediaPerson(Integer movieId, Integer mediaPersonId) throws DaoException;
    public abstract boolean deleteMovieCrew(Integer movieId) throws DaoException;
    public abstract boolean deleteMovieGenres(Integer movieId) throws DaoException;
    public abstract List<Movie> findByTitlePart(String movieTitle) throws DaoException;
}
