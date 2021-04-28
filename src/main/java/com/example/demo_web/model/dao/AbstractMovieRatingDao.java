package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.MovieRating;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public abstract class AbstractMovieRatingDao extends AbstractBaseDao<Integer, MovieRating>{
    public abstract List<MovieRating> findByMovieId(Integer id) throws DaoException;
    public abstract List<MovieRating> findByUserId(Integer id) throws DaoException;
    public abstract int countRatingsByMovieId(Integer movieId) throws DaoException;
    public abstract boolean exists(int ratingId, int movieId, int userId) throws DaoException;
    public abstract boolean isUnique(int movieId, int userId) throws DaoException;
}
