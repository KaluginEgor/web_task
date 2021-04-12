package com.example.demo_web.model.service.impl;

import com.example.demo_web.controller.command.RequestParameter;
import com.example.demo_web.controller.command.SessionRequestContent;
import com.example.demo_web.model.dao.MediaPersonDao;
import com.example.demo_web.model.dao.MovieDao;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.dao.impl.MediaPersonDaoImpl;
import com.example.demo_web.model.dao.impl.MovieDaoImpl;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.OccupationType;
import com.example.demo_web.model.service.MediaPersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MediaPersonServiceImpl implements MediaPersonService {
    private static final Logger logger = LogManager.getLogger(MediaPersonServiceImpl.class);
    private MediaPersonDao mediaPersonDao = MediaPersonDaoImpl.getInstance();
    private MovieDao movieDao = MovieDaoImpl.getInstance();

    @Override
    public List<MediaPerson> findAllBetween(int begin, int end) throws ServiceException {
        List<MediaPerson> allMediaPeopleBetween;
        try {
            allMediaPeopleBetween = mediaPersonDao.findAllBetween(begin, end);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return allMediaPeopleBetween;
    }

    @Override
    public List<MediaPerson> finaAll() throws ServiceException {
        List<MediaPerson> allMediaPeople;
        try {
            allMediaPeople = mediaPersonDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return allMediaPeople;
    }

    @Override
    public int countMediaPersons() throws ServiceException {
        int moviesCount;
        try {
            moviesCount = mediaPersonDao.countMediaPersons();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
        return moviesCount;
    }

    @Override
    public MediaPerson findById(Integer id) throws ServiceException {
        MediaPerson mediaPerson;
        try {
            mediaPerson = mediaPersonDao.findEntityById(id);
            List<Movie> movies = movieDao.findByActorId(id);
            mediaPerson.setMovies(movies);
            return mediaPerson;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public MediaPerson update(int id, String firstName, String secondName, String bio, OccupationType occupationType, LocalDate birthday, String picture, String[] moviesId) throws ServiceException {
        try {
            MediaPerson mediaPersonToUpdate = convertToMediaPerson(firstName, secondName, bio, occupationType, birthday, picture, moviesId);
            mediaPersonToUpdate.setId(id);
            MediaPerson updatedMediaPerson = mediaPersonDao.update(mediaPersonToUpdate);
            updatedMediaPerson.setMovies(mediaPersonToUpdate.getMovies());
            mediaPersonDao.deleteMediaPersonMovies(updatedMediaPerson.getId());
            for (Movie movie : updatedMediaPerson.getMovies()) {
                mediaPersonDao.insertMediaPersonMovie(updatedMediaPerson.getId(), movie.getId());
            }
            return updatedMediaPerson;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public MediaPerson create(String firstName, String secondName, String bio, OccupationType occupationType, LocalDate birthday, String picture, String[] moviesId) throws ServiceException {
        try {
            MediaPerson mediaPersonToCreate = convertToMediaPerson(firstName, secondName, bio, occupationType, birthday, picture, moviesId);
            MediaPerson createdMediaPerson = mediaPersonDao.create(mediaPersonToCreate);
            createdMediaPerson.setMovies(mediaPersonToCreate.getMovies());
            for (Movie movie : createdMediaPerson.getMovies()) {
                mediaPersonDao.insertMediaPersonMovie(createdMediaPerson.getId(), movie.getId());
            }
            return createdMediaPerson;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(int id) throws ServiceException {
        try {
            mediaPersonDao.delete(id);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private MediaPerson convertToMediaPerson(String firstName, String secondName, String bio, OccupationType occupationType, LocalDate birthday, String picture, String[] moviesId) throws DaoException {
        MediaPerson mediaPerson = new MediaPerson();
        mediaPerson.setFirstName(firstName);
        mediaPerson.setSecondName(secondName);
        mediaPerson.setBio(bio);
        mediaPerson.setOccupationType(occupationType);
        mediaPerson.setBirthday(birthday);
        mediaPerson.setPicture(picture);
        List<Movie> movies = new ArrayList<>();
        for (String movieId : moviesId) {
            Movie movie = movieDao.findEntityById(Integer.valueOf(movieId));
            movies.add(movie);
        }
        mediaPerson.setMovies(movies);
        return mediaPerson;
    }
}
