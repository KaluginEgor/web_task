package com.example.demo_web.model.service.impl;

import com.example.demo_web.controller.command.RequestParameter;
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
import java.util.*;
import java.util.stream.Collectors;

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
            errorMessage = Optional.of(ErrorMessage.INCORRECT_MEDIA_PERSON_ID_PARAMETER);
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
    public Map.Entry<Optional<MediaPerson>, Optional<String>> update(String stringId, String firstName, String secondName,
                                                                     String bio, String stringOccupationId, String stringBirthday,
                                                                     String picture, String[] stringMoviesId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<MediaPerson> mediaPerson = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        List<Integer> moviesId;
        if (stringMoviesId != null) {
            moviesId = Arrays.asList(stringMoviesId).stream().map(Integer::parseInt).collect(Collectors.toList());
        } else  {
            moviesId = new ArrayList<>();
        }
        if (!MediaPersonValidator.isValidId(stringId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_MEDIA_PERSON_ID_PARAMETER);
        } else {
            int mediaPersonId = Integer.valueOf(stringId);
            if (checkAllMoviesExist(moviesId)) {
                try {
                    transaction.initTransaction(mediaPersonDao, movieDao);
                    if (!mediaPersonDao.idExists(mediaPersonId)) {
                        errorMessage = Optional.of(ErrorMessage.TRY_UPDATE_NOT_EXISTING_MEDIA_PERSON);
                    } else {
                        MediaPerson mediaPersonToUpdate = convertToMediaPerson(firstName, secondName, bio, stringOccupationId, stringBirthday, picture);
                        mediaPersonToUpdate.setId(mediaPersonId);
                        List<Movie> movies = new ArrayList<>();
                        if (moviesId != null) {
                            for (Integer movieId : moviesId) {
                                Movie movie = movieDao.findEntityById(movieId);
                                movies.add(movie);
                            }
                        }
                        mediaPersonToUpdate.setMovies(movies);
                        mediaPerson = Optional.of(mediaPersonDao.update(mediaPersonToUpdate));
                        mediaPerson.get().setMovies(mediaPersonToUpdate.getMovies());
                        mediaPersonDao.deleteMediaPersonMovies(mediaPerson.get().getId());
                        for (Movie movie : mediaPerson.get().getMovies()) {
                            mediaPersonDao.insertMediaPersonMovie(mediaPerson.get().getId(), movie.getId());
                        }
                    }
                    transaction.commit();
                } catch (DaoException e) {
                    transaction.rollback();
                    throw new ServiceException(e);
                } finally {
                    transaction.endTransaction();
                }
            } else {
                errorMessage = Optional.of(ErrorMessage.SOME_MOVIES_ID_NOT_EXIST);
            }
        }
        return Map.entry(mediaPerson, errorMessage);
    }

    @Override
    public Map.Entry<Optional<MediaPerson>, Optional<String>> create(String firstName, String secondName, String bio,
                                                                     String stringOccupationId, String stringBirthday,
                                                                     String picture, String[] stringMoviesId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<MediaPerson> mediaPerson = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        List<Integer> moviesId;
        if (stringMoviesId != null) {
            moviesId = Arrays.asList(stringMoviesId).stream().map(Integer::parseInt).collect(Collectors.toList());
        } else  {
            moviesId = new ArrayList<>();
        }
        if (!checkAllMoviesExist(moviesId)) {
            errorMessage = Optional.of(ErrorMessage.SOME_MOVIES_ID_NOT_EXIST);
        } else {
            MediaPerson mediaPersonToCreate = convertToMediaPerson(firstName, secondName, bio, stringOccupationId, stringBirthday, picture);
            if (!isUnique(firstName, secondName, mediaPersonToCreate.getBirthday())) {
                errorMessage = Optional.of(ErrorMessage.MEDIA_PERSON_IS_NOT_UNIQUE);
            } else {
                try {
                    transaction.initTransaction(mediaPersonDao, movieDao);
                    mediaPerson = Optional.of(mediaPersonDao.create(mediaPersonToCreate));
                    List<Movie> movies = new ArrayList<>();
                    if (moviesId != null) {
                        for (Integer movieId : moviesId) {
                            Movie movie = movieDao.findEntityById(movieId);
                            movies.add(movie);
                        }
                    }
                    mediaPerson.get().setMovies(movies);
                    for (Movie movie : mediaPerson.get().getMovies()) {
                        mediaPersonDao.insertMediaPersonMovie(mediaPerson.get().getId(), movie.getId());
                    }
                    transaction.commit();
                } catch (DaoException e) {
                    transaction.rollback();
                    throw new ServiceException(e);
                } finally {
                    transaction.endTransaction();
                }
            }
        }
        return Map.entry(mediaPerson, errorMessage);
    }

    @Override
    public Optional<String> delete(String stringId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<String> errorMessage = Optional.empty();
        if (!MediaPersonValidator.isValidId(stringId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_DELETE_MEDIA_PERSON_PARAMETERS);
        } else {
            try {
                transaction.init(mediaPersonDao);
                int mediaPersonId = Integer.valueOf(stringId);
                if (mediaPersonDao.idExists(mediaPersonId)) {
                    mediaPersonDao.delete(mediaPersonId);
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_DELETE_NOT_EXISTING_MEDIA_PERSON);
                }
            } catch (DaoException e) {
                throw new ServiceException(e);
            } finally {
                transaction.end();
            }
        }
        return errorMessage;
    }

    @Override
    public Map.Entry<List<String>, List<String>> validateData(String firstName, String secondName, String bio, String stringOccupationId,
                                                              String stringBirthday, String picture, String[] stringMoviesId) {
        Map<String, Boolean> validationResult = MediaPersonValidator.validateData(firstName, secondName, bio,
                stringOccupationId, stringBirthday, picture, stringMoviesId);
        List<String> errorMessages = defineValidationErrorMessages(validationResult);
        List<String> validParameters = defineValidParameters(validationResult);
        return Map.entry(validParameters, errorMessages);
    }

    private static List<String> defineValidParameters(Map<String, Boolean> map) {
        return map.entrySet().stream().filter(entry -> Boolean.TRUE.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private static List<String> defineValidationErrorMessages(Map<String, Boolean> map) {
        List<String> errorMessages = new ArrayList<>();
        map.entrySet().stream().filter(entry -> Boolean.FALSE.equals(entry.getValue())).forEach(entry ->
                errorMessages.add(defineErrorValidationMessage(entry)));
        return errorMessages;
    }

    private static String defineErrorValidationMessage(Map.Entry<String, Boolean> entry) {
        String message = null;
        switch (entry.getKey()) {
            case RequestParameter.FIRST_NAME:
                message = ErrorMessage.NOT_VALID_FIRST_NAME;
                break;
            case RequestParameter.SECOND_NAME:
                message = ErrorMessage.NOT_VALID_SECOND_NAME;
                break;
            case RequestParameter.BIO:
                message = ErrorMessage.NOT_VALID_BIO;
                break;
            case RequestParameter.MEDIA_PERSON_OCCUPATION_TYPE:
                message = ErrorMessage.NOT_VALID_OCCUPATION;
                break;
            case RequestParameter.MEDIA_PERSON_BIRTHDAY:
                message = ErrorMessage.NOT_VALID_BIRTHDAY;
                break;
            case RequestParameter.PICTURE:
                message = ErrorMessage.NOT_VALID_PICTURE;
                break;
            case RequestParameter.MEDIA_PERSON_MOVIES:
                message = ErrorMessage.NOT_VALID_MOVIES_ID;
                break;
        }
        return message;
    }

    private boolean checkAllMoviesExist(List<Integer> moviesId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        if (!moviesId.isEmpty()) {
            try {
                transaction.init(movieDao);
                for (Integer movieId : moviesId) {
                    if (!movieDao.idExists(movieId)) {
                        return false;
                    }
                }
                return true;
            } catch (DaoException e) {
                logger.error(e);
                throw new ServiceException(e);
            } finally {
                transaction.end();
            }
        } else {
            return true;
        }
    }

    private boolean isUnique(String firstName, String secondName, LocalDate birthday) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        try {
            transaction.init(mediaPersonDao);
            return mediaPersonDao.isUnique(firstName, secondName, birthday);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    private MediaPerson convertToMediaPerson(String firstName, String secondName, String bio,
                                             String stringOccupationId, String stringBirthday, String picture) {
        MediaPerson mediaPerson = new MediaPerson();
        mediaPerson.setFirstName(firstName);
        mediaPerson.setSecondName(secondName);
        mediaPerson.setBio(bio);
        int occupationId = Integer.valueOf(stringOccupationId);
        OccupationType occupationType = OccupationType.values()[occupationId];
        mediaPerson.setOccupationType(occupationType);
        LocalDate birthday = LocalDate.parse(stringBirthday);
        mediaPerson.setBirthday(birthday);
        mediaPerson.setPicture(picture);
        return mediaPerson;
    }
}
