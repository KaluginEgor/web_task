package com.example.demo_web.dao;

import com.example.demo_web.entity.MovieRating;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public interface MovieRatingDao extends BaseDao<Integer, MovieRating> {
    List<MovieRating> findByMovieId(Integer id) throws DaoException;
}
