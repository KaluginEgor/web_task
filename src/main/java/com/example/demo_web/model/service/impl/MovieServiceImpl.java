package com.example.demo_web.model.service.impl;

import com.example.demo_web.model.dao.MediaPersonDao;
import com.example.demo_web.model.dao.MovieDao;
import com.example.demo_web.model.dao.MovieRatingDao;
import com.example.demo_web.model.dao.MovieReviewDao;

import com.example.demo_web.model.dao.impl.MediaPersonDaoImpl;
import com.example.demo_web.model.dao.impl.MovieDaoImpl;
import com.example.demo_web.model.dao.impl.MovieRatingDaoImpl;
import com.example.demo_web.model.dao.impl.MovieReviewDaoImpl;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.*;
import com.example.demo_web.model.service.MovieService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MovieServiceImpl implements MovieService {
    private static final Logger logger = LogManager.getLogger(MovieServiceImpl.class);
    private MovieDao movieDao = MovieDaoImpl.getInstance();
    private MediaPersonDao mediaPersonDao = MediaPersonDaoImpl.getInstance();
    private MovieReviewDao reviewDao = MovieReviewDaoImpl.getInstance();
    private MovieRatingDao ratingDao = MovieRatingDaoImpl.getInstance();

    @Override
    public List<Movie> findAllBetween(int begin, int end) throws ServiceException {
        List<Movie> allUsers;
        try {
            allUsers = new ArrayList<>(movieDao.findAllBetween(begin, end));
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
        return allUsers;
    }

    @Override
    public int countMovies() throws ServiceException {
        int moviesCount;
        try {
            moviesCount = movieDao.countMovies();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
        return moviesCount;
    }

    @Override
    public Movie findById(Integer id) throws ServiceException {
        Movie movie = null;
        try {
            movie = movieDao.findEntityById(id);
            List<GenreType> genreTypes = movieDao.findGenreTypesByMovieId(id);
            movie.setGenres(genreTypes);
            List<MediaPerson> crew = mediaPersonDao.findByMovieId(id);
            movie.setCrew(crew);
            List<MovieReview> movieReviews = reviewDao.findByMovieId(id);
            movie.setReviews(movieReviews);
            List<MovieRating> ratingList = ratingDao.findByMovieId(id);
            movie.setRatingList(ratingList);
            return movie;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }


    @Override
    public Map<Integer, String> findAllTitles() throws ServiceException {
        Map<Integer, String> foundTitles = new LinkedHashMap<>();
        try {
            foundTitles = movieDao.findAllTitles();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return foundTitles;
    }
}
