package com.example.demo_web.model.service.impl;

import com.example.demo_web.model.dao.*;

import com.example.demo_web.model.dao.impl.MediaPersonDao;
import com.example.demo_web.model.dao.impl.MovieDao;
import com.example.demo_web.model.dao.impl.MovieRatingDao;
import com.example.demo_web.model.dao.impl.MovieReviewDao;
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
    private AbstractMovieDao abstractMovieDao = MovieDao.getInstance();
    private AbstractMediaPersonDao abstractMediaPersonDao = MediaPersonDao.getInstance();
    private AbstractMovieReviewDao reviewDao = MovieReviewDao.getInstance();
    private AbstractMovieRatingDao ratingDao = MovieRatingDao.getInstance();
    private static final String DEFAULT_MOVIE_PICTURE = "C:/Epam/pictures/movie.jpg";

    @Override
    public List<Movie> findAllBetween(int begin, int end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieDao);
        List<Movie> allUsers;
        try {
            allUsers = new ArrayList<>(abstractMovieDao.findAllBetween(begin, end));
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return allUsers;
    }

    @Override
    public int countMovies() throws ServiceException {
        int moviesCount;
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieDao);
        try {
            moviesCount = abstractMovieDao.countMovies();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return moviesCount;
    }

    @Override
    public Movie findById(Integer id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.initTransaction(abstractMovieDao, abstractMediaPersonDao, reviewDao, ratingDao);
        Movie movie;
        try {
            movie = abstractMovieDao.findEntityById(id);
            List<GenreType> genreTypes = abstractMovieDao.findGenreTypesByMovieId(id);
            movie.setGenres(genreTypes);
            List<MediaPerson> crew = abstractMediaPersonDao.findByMovieId(id);
            movie.setCrew(crew);
            List<MovieReview> movieReviews = reviewDao.findByMovieId(id);
            movie.setReviews(movieReviews);
            List<MovieRating> ratingList = ratingDao.findByMovieId(id);
            movie.setRatingList(ratingList);
            transaction.commit();
            return movie;
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.endTransaction();
        }
    }


    @Override
    public List<Movie> findAll() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieDao);
        List<Movie> movies = new ArrayList<>();
        try {
            movies = abstractMovieDao.findAll();
            return movies;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public Movie create(String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.initTransaction(abstractMovieDao, reviewDao, ratingDao);
        try {
            if (picture.isEmpty()) {
                picture = DEFAULT_MOVIE_PICTURE;
            }
            Movie movieToCreate = convertToMovie(title, description, releaseDate, picture, stringGenres, stringMediaPersonsId);
            Movie createdMovie = abstractMovieDao.create(movieToCreate);
            createdMovie.setGenres(movieToCreate.getGenres());
            createdMovie.setCrew(movieToCreate.getCrew());
            for (GenreType genre : createdMovie.getGenres()) {
                abstractMovieDao.insertMovieGenre(createdMovie.getId(), genre.ordinal());
            }
            for (MediaPerson mediaPerson : createdMovie.getCrew()) {
                abstractMovieDao.insertMovieMediaPerson(createdMovie.getId(), mediaPerson.getId());
            }
            List<MovieReview> movieReviews = reviewDao.findByMovieId(createdMovie.getId());
            createdMovie.setReviews(movieReviews);
            List<MovieRating> ratingList = ratingDao.findByMovieId(createdMovie.getId());
            createdMovie.setRatingList(ratingList);
            transaction.commit();
            return createdMovie;
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.endTransaction();
        }
    }

    @Override
    public Movie update(int id, String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.initTransaction(abstractMovieDao, reviewDao, ratingDao);
        try {
            if (picture.isEmpty()) {
                picture = DEFAULT_MOVIE_PICTURE;
            }
            Movie movieToUpdate = convertToMovie(title, description, releaseDate, picture, stringGenres, stringMediaPersonsId);
            movieToUpdate.setId(id);
            abstractMovieDao.update(movieToUpdate);
            abstractMovieDao.deleteMovieGenres(movieToUpdate.getId());
            for (GenreType genre : movieToUpdate.getGenres()) {
                abstractMovieDao.insertMovieGenre(movieToUpdate.getId(), genre.ordinal());
            }
            abstractMovieDao.deleteMovieCrew(movieToUpdate.getId());
            for (MediaPerson mediaPerson : movieToUpdate.getCrew()) {
                abstractMovieDao.insertMovieMediaPerson(movieToUpdate.getId(), mediaPerson.getId());
            }
            List<MovieReview> movieReviews = reviewDao.findByMovieId(movieToUpdate.getId());
            movieToUpdate.setReviews(movieReviews);
            List<MovieRating> ratingList = ratingDao.findByMovieId(movieToUpdate.getId());
            movieToUpdate.setRatingList(ratingList);
            transaction.commit();
            return movieToUpdate;
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.endTransaction();
        }
    }

    @Override
    public boolean delete(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieDao);
        try {
            abstractMovieDao.delete(id);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public List<Movie> findByNamePart(String title) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieDao);
        try {
            return abstractMovieDao.findByTitlePart(title);
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    private Movie convertToMovie(String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws DaoException {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setReleaseDate(releaseDate);
        movie.setPicture(picture);
        List<GenreType> genres = new ArrayList<>();
        if (stringGenres != null) {
            for (String stringGenre : stringGenres) {
                GenreType genreType = GenreType.valueOf(stringGenre);
                genres.add(genreType);
            }
        }
        movie.setGenres(genres);
        List<MediaPerson> mediaPersons = new ArrayList<>();
        if (stringMediaPersonsId != null) {
            for (String stringId : stringMediaPersonsId) {
                MediaPerson mediaPerson = abstractMediaPersonDao.findEntityById(Integer.valueOf(stringId));
                mediaPersons.add(mediaPerson);
            }
        }
        movie.setCrew(mediaPersons);
        return movie;
    }
}
