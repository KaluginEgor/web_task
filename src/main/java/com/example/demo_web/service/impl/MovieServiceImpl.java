package com.example.demo_web.service.impl;

import com.example.demo_web.dao.MediaPersonDao;
import com.example.demo_web.dao.MovieDao;
import com.example.demo_web.dao.MovieRatingDao;
import com.example.demo_web.dao.MovieReviewDao;
import com.example.demo_web.dao.impl.MediaPersonDaoImpl;
import com.example.demo_web.dao.impl.MovieDaoImpl;
import com.example.demo_web.dao.impl.MovieRatingDaoImpl;
import com.example.demo_web.dao.impl.MovieReviewDaoImpl;
import com.example.demo_web.entity.*;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.service.MovieService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceImpl implements MovieService {
    private static final Logger logger = LogManager.getLogger(MovieServiceImpl.class);
    private MovieDao movieDao = MovieDaoImpl.getInstance();
    private MediaPersonDao mediaPersonDao = MediaPersonDaoImpl.getInstance();
    private MovieReviewDao reviewDao = MovieReviewDaoImpl.getInstance();
    private MovieRatingDao ratingDao = MovieRatingDaoImpl.getInstance();

    @Override
    public List<Movie> findAll(int begin, int end) throws ServiceException {
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
            movie.setGenre(genreTypes);
            List<MediaPerson> crew = mediaPersonDao.findByMovieId(id);
            movie.setCrew(crew);
            List<MovieReview> movieReviews = reviewDao.findByMovieId(id);
            movie.setMovieReviews(movieReviews);
            List<MovieRating> ratingList = ratingDao.findByMovieId(id);
            movie.setRatingList(ratingList);
            return movie;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
