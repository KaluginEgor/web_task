package com.example.demo_web.model.service.impl;

import com.example.demo_web.model.dao.EntityTransaction;
import com.example.demo_web.model.dao.AbstractMediaPersonDao;
import com.example.demo_web.model.dao.AbstractMovieDao;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.dao.impl.MediaPersonDao;
import com.example.demo_web.model.dao.impl.MovieDao;
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
    private AbstractMediaPersonDao abstractMediaPersonDao = MediaPersonDao.getInstance();
    private AbstractMovieDao abstractMovieDao = MovieDao.getInstance();
    private static final String DEFAULT_MEDIA_PERSON_PICTURE = "C:/Epam/pictures/media_person.jpg";

    @Override
    public List<MediaPerson> findAllBetween(int begin, int end) throws ServiceException {
        List<MediaPerson> allMediaPeopleBetween;
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMediaPersonDao);
        try {
            allMediaPeopleBetween = abstractMediaPersonDao.findAllBetween(begin, end);
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return allMediaPeopleBetween;
    }

    @Override
    public List<MediaPerson> finaAll() throws ServiceException {
        List<MediaPerson> allMediaPeople;
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMediaPersonDao);
        try {
            allMediaPeople = abstractMediaPersonDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return allMediaPeople;
    }

    @Override
    public int countMediaPersons() throws ServiceException {
        int mediaPersonsCount;
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMediaPersonDao);
        try {
            mediaPersonsCount = abstractMediaPersonDao.countMediaPersons();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return mediaPersonsCount;
    }

    @Override
    public MediaPerson findById(Integer id) throws ServiceException {
        MediaPerson mediaPerson;
        EntityTransaction transaction = new EntityTransaction();
        transaction.initTransaction(abstractMediaPersonDao, abstractMovieDao);
        try {
            mediaPerson = abstractMediaPersonDao.findEntityById(id);
            List<Movie> movies = abstractMovieDao.findByActorId(id);
            mediaPerson.setMovies(movies);
            transaction.commit();
            return mediaPerson;
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.endTransaction();
        }
    }

    @Override
    public MediaPerson update(int id, String firstName, String secondName, String bio, OccupationType occupationType, LocalDate birthday, String picture, String[] moviesId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMediaPersonDao);
        try {
            if (picture.isEmpty()) {
                picture = DEFAULT_MEDIA_PERSON_PICTURE;
            }
            MediaPerson mediaPersonToUpdate = convertToMediaPerson(firstName, secondName, bio, occupationType, birthday, picture, moviesId);
            mediaPersonToUpdate.setId(id);
            MediaPerson updatedMediaPerson = abstractMediaPersonDao.update(mediaPersonToUpdate);
            updatedMediaPerson.setMovies(mediaPersonToUpdate.getMovies());
            abstractMediaPersonDao.deleteMediaPersonMovies(updatedMediaPerson.getId());
            for (Movie movie : updatedMediaPerson.getMovies()) {
                abstractMediaPersonDao.insertMediaPersonMovie(updatedMediaPerson.getId(), movie.getId());
            }
            return updatedMediaPerson;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public MediaPerson create(String firstName, String secondName, String bio, OccupationType occupationType, LocalDate birthday, String picture, String[] moviesId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMediaPersonDao);
        try {
            if (picture.isEmpty()) {
                picture = DEFAULT_MEDIA_PERSON_PICTURE;
            }
            MediaPerson mediaPersonToCreate = convertToMediaPerson(firstName, secondName, bio, occupationType, birthday, picture, moviesId);
            MediaPerson createdMediaPerson = abstractMediaPersonDao.create(mediaPersonToCreate);
            createdMediaPerson.setMovies(mediaPersonToCreate.getMovies());
            for (Movie movie : createdMediaPerson.getMovies()) {
                abstractMediaPersonDao.insertMediaPersonMovie(createdMediaPerson.getId(), movie.getId());
            }
            return createdMediaPerson;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public boolean delete(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMediaPersonDao);
        try {
            abstractMediaPersonDao.delete(id);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
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
        if (moviesId != null) {
            for (String movieId : moviesId) {
                Movie movie = abstractMovieDao.findEntityById(Integer.valueOf(movieId));
                movies.add(movie);
            }
        }
        mediaPerson.setMovies(movies);
        return mediaPerson;
    }
}
