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
import com.example.demo_web.model.util.message.ErrorMessage;
import com.example.demo_web.model.validator.MediaPersonValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MediaPersonServiceImpl implements MediaPersonService {
    private static final MediaPersonService instance = new MediaPersonServiceImpl();
    private static final Logger logger = LogManager.getLogger(MediaPersonServiceImpl.class);
    private AbstractMediaPersonDao mediaPersonDao = MediaPersonDao.getInstance();
    private AbstractMovieDao movieDao = MovieDao.getInstance();
    private static final String DEFAULT_MEDIA_PERSON_PICTURE = "C:/Epam/pictures/media_person.jpg";

    private MediaPersonServiceImpl() {};

    public static MediaPersonService getInstance() {
        return instance;
    }

    @Override
    public List<MediaPerson> findAllBetween(int begin, int end) throws ServiceException {
        List<MediaPerson> allMediaPeopleBetween;
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(mediaPersonDao);
        try {
            allMediaPeopleBetween = mediaPersonDao.findAllBetween(begin, end);
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
        transaction.init(mediaPersonDao);
        try {
            allMediaPeople = mediaPersonDao.findAll();
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
        transaction.init(mediaPersonDao);
        try {
            mediaPersonsCount = mediaPersonDao.countMediaPersons();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return mediaPersonsCount;
    }

    @Override
    public Map.Entry<Optional<MediaPerson>, Optional<String>> findById(String stringMediaPersonId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<MediaPerson> mediaPerson = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        if (!MediaPersonValidator.isValidId(stringMediaPersonId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_FIND_MEDIA_PERSON_PARAMETERS);
        } else {
            try {
                transaction.initTransaction(mediaPersonDao, movieDao);
                int mediaPersonId = Integer.valueOf(stringMediaPersonId);
                if (mediaPersonDao.idExists(mediaPersonId)) {
                    mediaPerson = Optional.of(mediaPersonDao.findEntityById(mediaPersonId));
                    List<Movie> movies = movieDao.findByActorId(mediaPersonId);
                    mediaPerson.get().setMovies(movies);
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_FIND_NOT_EXISTING_MEDIA_PERSON);
                }
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
                throw new ServiceException(e);
            } finally {
                transaction.endTransaction();
            }
        }
        return Map.entry(mediaPerson, errorMessage);
    }

    @Override
    public MediaPerson update(int id, String firstName, String secondName, String bio, OccupationType occupationType, LocalDate birthday, String picture, String[] moviesId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(mediaPersonDao);
        try {
            if (picture.isEmpty()) {
                picture = DEFAULT_MEDIA_PERSON_PICTURE;
            }
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
        } finally {
            transaction.end();
        }
    }

    @Override
    public MediaPerson create(String firstName, String secondName, String bio, OccupationType occupationType, LocalDate birthday, String picture, String[] moviesId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(mediaPersonDao);
        try {
            if (picture.isEmpty()) {
                picture = DEFAULT_MEDIA_PERSON_PICTURE;
            }
            MediaPerson mediaPersonToCreate = convertToMediaPerson(firstName, secondName, bio, occupationType, birthday, picture, moviesId);
            MediaPerson createdMediaPerson = mediaPersonDao.create(mediaPersonToCreate);
            createdMediaPerson.setMovies(mediaPersonToCreate.getMovies());
            for (Movie movie : createdMediaPerson.getMovies()) {
                mediaPersonDao.insertMediaPersonMovie(createdMediaPerson.getId(), movie.getId());
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
        transaction.init(mediaPersonDao);
        try {
            mediaPersonDao.delete(id);
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
                Movie movie = movieDao.findEntityById(Integer.valueOf(movieId));
                movies.add(movie);
            }
        }
        mediaPerson.setMovies(movies);
        return mediaPerson;
    }
}
