package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.GenreType;
import com.epam.project.model.entity.Movie;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public abstract class AbstractMovieDao extends AbstractBaseDao<Integer, Movie> {
    public abstract List<Movie> findAllBetween(int begin, int end) throws DaoException;
    public abstract Map<Integer, String> findAllTitles() throws DaoException;
    public abstract int countMovies() throws DaoException;
    public abstract List<GenreType> findGenreTypesByMovieId(Integer id) throws DaoException;
    public abstract List<Movie> findByActorId(Integer id) throws DaoException;
    public abstract boolean insertMovieGenre(Integer movieId, Integer genreId) throws DaoException;
    public abstract boolean insertMovieMediaPerson(Integer movieId, Integer mediaPersonId) throws DaoException;
    public abstract boolean deleteMovieCrew(Integer movieId) throws DaoException;
    public abstract boolean deleteMovieGenres(Integer movieId) throws DaoException;
    public abstract List<Movie> findByTitlePart(String movieTitle) throws DaoException;
    public abstract float findRatingById(Integer movieId) throws DaoException;
    public abstract boolean updateRatingById(float rating, Integer movieId) throws DaoException;
    public abstract boolean idExists(int id) throws DaoException;
    public abstract boolean isUnique(String title, LocalDate releaseDate) throws DaoException;
}
