package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.MovieRating;

import java.util.List;

public abstract class AbstractMovieRatingDao extends AbstractBaseDao<Integer, MovieRating>{
    public abstract List<MovieRating> findByMovieId(Integer id) throws DaoException;
    public abstract List<MovieRating> findByUserId(Integer id) throws DaoException;
    public abstract int countRatingsByMovieId(Integer movieId) throws DaoException;
    public abstract boolean exists(int ratingId, int movieId, int userId) throws DaoException;
    public abstract boolean isUnique(int movieId, int userId) throws DaoException;
}
