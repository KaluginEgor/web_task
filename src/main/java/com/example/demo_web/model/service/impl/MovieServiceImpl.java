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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        Movie movie;
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
    public List<Movie> findAll() throws ServiceException {
        List<Movie> movies = new ArrayList<>();
        try {
            movies = movieDao.findAll();
            return movies;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Movie create(String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws ServiceException {
        try {
            Movie movieToCreate = convertToMovie(title, description, releaseDate, picture, stringGenres, stringMediaPersonsId);
            Movie createdMovie = movieDao.create(movieToCreate);
            createdMovie.setGenres(movieToCreate.getGenres());
            createdMovie.setCrew(movieToCreate.getCrew());
            for (GenreType genre : createdMovie.getGenres()) {
                movieDao.insertMovieGenre(createdMovie.getId(), genre.ordinal());
            }
            for (MediaPerson mediaPerson : createdMovie.getCrew()) {
                movieDao.insertMovieMediaPerson(createdMovie.getId(), mediaPerson.getId());
            }
            List<MovieReview> movieReviews = reviewDao.findByMovieId(createdMovie.getId());
            createdMovie.setReviews(movieReviews);
            List<MovieRating> ratingList = ratingDao.findByMovieId(createdMovie.getId());
            createdMovie.setRatingList(ratingList);
            return createdMovie;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Movie update(int id, String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws ServiceException {
        try {
            Movie movieToUpdate = convertToMovie(title, description, releaseDate, picture, stringGenres, stringMediaPersonsId);
            movieToUpdate.setId(id);
            movieDao.update(movieToUpdate);
            movieDao.deleteMovieGenres(movieToUpdate.getId());
            for (GenreType genre : movieToUpdate.getGenres()) {
                movieDao.insertMovieGenre(movieToUpdate.getId(), genre.ordinal());
            }
            movieDao.deleteMovieCrew(movieToUpdate.getId());
            for (MediaPerson mediaPerson : movieToUpdate.getCrew()) {
                movieDao.insertMovieMediaPerson(movieToUpdate.getId(), mediaPerson.getId());
            }
            List<MovieReview> movieReviews = reviewDao.findByMovieId(movieToUpdate.getId());
            movieToUpdate.setReviews(movieReviews);
            List<MovieRating> ratingList = ratingDao.findByMovieId(movieToUpdate.getId());
            movieToUpdate.setRatingList(ratingList);
            return movieToUpdate;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private Movie convertToMovie(String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws DaoException {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setReleaseDate(releaseDate);
        movie.setPicture(picture);
        List<GenreType> genres = new ArrayList<>();
        for (String stringGenre : stringGenres) {
            GenreType genreType = GenreType.valueOf(stringGenre);
            genres.add(genreType);
        }
        movie.setGenres(genres);
        List<MediaPerson> mediaPersons = new ArrayList<>();
        for (String stringId : stringMediaPersonsId) {
            MediaPerson mediaPerson = mediaPersonDao.findEntityById(Integer.valueOf(stringId));
            mediaPersons.add(mediaPerson);
        }
        movie.setCrew(mediaPersons);
        return movie;
    }

    @Override
    public boolean delete(int id) throws ServiceException {
        try {
            movieDao.delete(id);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
