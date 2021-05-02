package com.epam.project.model.service.impl;

import com.epam.project.exception.DaoException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.dao.*;
import com.epam.project.model.entity.*;
import com.epam.project.model.service.MovieRatingService;
import com.epam.project.model.service.UserService;
import com.epam.project.model.util.mail.MailBuilder;
import com.epam.project.model.util.mail.MailSender;
import com.epam.project.model.util.message.ErrorMessage;
import com.epam.project.model.validator.MediaPersonValidator;
import com.epam.project.model.validator.UserValidator;
import com.epam.project.model.validator.ValidationHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final UserService instance = new UserServiceImpl();
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private static final String DEFAULT_USER_PICTURE = "C:/Epam/pictures/user.jpg";

    private final AbstractMovieRatingDao ratingDao = MovieRatingDao.getInstance();
    private final AbstractMovieReviewDao reviewDao = MovieReviewDao.getInstance();
    private final AbstractUserDao userDao = UserDao.getInstance();
    private final MovieRatingService ratingService = MovieRatingServiceImpl.getInstance();

    private UserServiceImpl() {}

    public static UserService getInstance() {
        return instance;
    }

    @Override
    public Map.Entry<Optional<User>, Optional<String>> login(String login, String password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<User> user = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        try {
            transaction.initTransaction(userDao, reviewDao, ratingDao);
            if (!userDao.loginExists(login)) {
                errorMessage = Optional.of(ErrorMessage.LOGIN_NOT_EXIST);
            } else {
                String encryptedPassword = DigestUtils.md5Hex(password);
                String passwordFromDB = userDao.findPasswordByLogin(login);
                if (!encryptedPassword.equals(passwordFromDB)) {
                    errorMessage = Optional.of(ErrorMessage.ENTERED_PASSWORD_INCORRECT_ERROR_MESSAGE);
                } else {
                    User foundUser = userDao.findUserByLogin(login);
                    if (!UserState.ACTIVE.equals(foundUser.getState())) {
                        if (UserState.INACTIVE.equals(foundUser.getState())) {
                            errorMessage = Optional.of(ErrorMessage.INACTIVE_USER);
                        }
                        if (UserState.BLOCKED.equals(foundUser.getState())) {
                            errorMessage = Optional.of(ErrorMessage.BLOCKED_USER);
                        }
                    } else {
                        user = Optional.of(foundUser);
                        List<MovieReview> reviews = reviewDao.findByUserId(user.get().getId());
                        user.get().setMovieReviews(reviews);
                        List<MovieRating> ratings = ratingDao.findByUserId(user.get().getId());
                        user.get().setMovieRatings(ratings);
                    }
                }
            }
            transaction.commit();
        } catch (DaoException e) {
            logger.error(e);
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.endTransaction();
        }
        return Map.entry(user, errorMessage);
    }

    @Override
    public Map.Entry<Optional<User>, Optional<String>> register(String login, String email, String firstName, String secondName, String password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<User> registeredUser = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        User userToRegister = new User();
        userToRegister.setLogin(login);
        userToRegister.setEmail(email);
        userToRegister.setFirstName(firstName);
        userToRegister.setSecondName(secondName);
        try {
            transaction.init(userDao);
            if (userDao.loginExists(login)) {
                errorMessage = Optional.of(ErrorMessage.LOGIN_IS_NOT_UNIQUE);
            } else {
                String encryptedPassword = DigestUtils.md5Hex(password);
                setDefaultFields(userToRegister);
                registeredUser = Optional.of(userDao.create(userToRegister, encryptedPassword));
            }
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return Map.entry(registeredUser, errorMessage);
    }

    @Override
    public Optional<String> activate(String stringId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<String> errorMessage = Optional.empty();
        if (!UserValidator.isValidId(stringId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_ACTIVATE_USER_PARAMETERS);
        } else {
            try {
                transaction.init(userDao);
                int id = Integer.parseInt(stringId);
                if (userDao.idExists(id)) {
                    userDao.activateUser(id);
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_ACTIVATE_NOT_EXISTING_USER);
                }
            } catch (DaoException e) {
                logger.error(e);
                throw new ServiceException(e);
            } finally {
                transaction.end();
            }
        }
        return errorMessage;
    }

    @Override
    public UserState detectStateById(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(userDao);
        UserState userState;
        try {
            userState = userDao.detectStateById(id);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return userState;
    }

    @Override
    public boolean idExists(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        try {
            transaction.init(userDao);
            return userDao.idExists(id);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public Map.Entry<Optional<User>, Optional<String>> update(String stringId, String email, String firstName, String secondName, String picture) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<User> user = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        if (!UserValidator.isValidId(stringId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_UPDATE_USER_ID_PARAMETER);
        } else {
            int userId = Integer.parseInt(stringId);
            try {
                transaction.initTransaction(userDao, reviewDao, ratingDao);
                if (!userDao.idExists(userId)) {
                    errorMessage = Optional.of(ErrorMessage.TRY_UPDATE_NOT_EXISTING_USER);
                } else {
                    User userToUpdate = userDao.findEntityById(userId);
                    userToUpdate.setId(userId);
                    userToUpdate.setEmail(email);
                    userToUpdate.setFirstName(firstName);
                    userToUpdate.setSecondName(secondName);
                    userToUpdate.setPicture(picture);
                    userDao.update(userToUpdate);
                    List<MovieReview> reviews = reviewDao.findByUserId(userId);
                    userToUpdate.setMovieReviews(reviews);
                    List<MovieRating> ratings = ratingDao.findByUserId(userId);
                    userToUpdate.setMovieRatings(ratings);
                    user = Optional.of(userToUpdate);
                }
                transaction.commit();
            } catch (DaoException e) {
                logger.error(e);
                transaction.rollback();
                throw new ServiceException(e);
            } finally {
                transaction.endTransaction();
            }
        }
        return Map.entry(user, errorMessage);
    }

    @Override
    public List<User> findAllBetween(int begin, int end) throws ServiceException {
        List<User> allUsersBetween;
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(userDao);
        try {
            allUsersBetween = userDao.findAllBetween(begin, end);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return allUsersBetween;
    }

    @Override
    public Optional<String> block(String stringId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<String> errorMessage = Optional.empty();
        if (!UserValidator.isValidId(stringId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_BLOCK_USER_PARAMETERS);
        } else {
            try {
                transaction.init(userDao);
                int id = Integer.parseInt(stringId);
                if (userDao.idExists(id)) {
                    String stringRole = userDao.findRoleById(id);
                    if (!UserRole.ADMIN.equals(UserRole.valueOf(stringRole))) {
                        userDao.blockUser(id);
                    } else {
                        errorMessage = Optional.of(ErrorMessage.TRY_BLOCK_ADMIN);
                    }
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_BLOCK_NOT_EXISTING_USER);
                }
            } catch (DaoException e) {
                logger.error(e);
                throw new ServiceException(e);
            } finally {
                transaction.end();
            }
        }
        return errorMessage;
    }

    @Override
    public int countUsers() throws ServiceException {
        int usersCount;
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(userDao);
        try {
            usersCount = userDao.countUsers();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return usersCount;
    }

    @Override
    public Map.Entry<Optional<User>, Optional<String>> findById(String stringUserId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<User> user = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        if (!MediaPersonValidator.isValidId(stringUserId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_FIND_USER_ID_PARAMETER);
        } else {
            try {
                transaction.initTransaction(userDao, reviewDao, ratingDao);
                int userId = Integer.parseInt(stringUserId);
                if (userDao.idExists(userId)) {
                    user = Optional.of(userDao.findEntityById(userId));
                    List<MovieReview> reviews = reviewDao.findByUserId(userId);
                    user.get().setMovieReviews(reviews);
                    List<MovieRating> ratings = ratingDao.findByUserId(userId);
                    user.get().setMovieRatings(ratings);
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_FIND_NOT_EXISTING_USER);
                }
                transaction.commit();
            } catch (DaoException e) {
                logger.error(e);
                transaction.rollback();
                throw new ServiceException(e);
            } finally {
                transaction.endTransaction();
            }
        }
        return Map.entry(user, errorMessage);
    }

    @Override
    public Optional<String> delete(String stringId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<String> errorMessage = Optional.empty();
        if (!UserValidator.isValidId(stringId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_DELETE_USER_ID_PARAMETER);
        } else {
            try {
                transaction.initTransaction(userDao, ratingDao);
                int id = Integer.parseInt(stringId);
                if (userDao.idExists(id)) {
                    String stringRole = userDao.findRoleById(id);
                    if (!UserRole.ADMIN.equals(UserRole.valueOf(stringRole))) {
                        for (MovieRating rating : ratingDao.findByUserId(id)) {
                            ratingService.delete(Integer.toString(rating.getId()), Integer.toString(rating.getMovieId()),
                                    Integer.toString(rating.getUserId()));
                        }
                        userDao.delete(id);
                    } else {
                        errorMessage = Optional.of(ErrorMessage.TRY_DELETE_ADMIN);
                    }
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_DELETE_NOT_EXISTING_USER);
                }
                transaction.commit();
            } catch (DaoException e) {
                logger.error(e);
                transaction.rollback();
                throw new ServiceException(e);
            } finally {
                transaction.endTransaction();
            }
        }
        return errorMessage;
    }

    @Override
    public void constructAndSendConfirmEmail(String lang, User user) {
        String emailSubject = MailBuilder.buildEmailSubject(lang);
        String emailBody = MailBuilder.buildEmailBody(user,lang);
        MailSender mailSender = new MailSender(emailSubject, emailBody, user.getEmail());
        mailSender.send();
    }

    @Override
    public Map.Entry<List<String>, List<String>> validateUpdateData(String email, String firstName, String secondName, String picture) {
        Map<String, Boolean> validationResult = UserValidator.validateUpdateData(email, firstName, secondName, picture);
        List<String> errorMessages = ValidationHelper.defineValidationErrorMessages(validationResult);
        List<String> validParameters = ValidationHelper.defineValidParameters(validationResult);
        return Map.entry(validParameters, errorMessages);
    }

    @Override
    public Map.Entry<List<String>, List<String>> validateRegistrationData(String login, String email, String firstName,
                                                                          String secondName, String password){
        Map<String, Boolean> validationResult = UserValidator.validateRegistrationData(login, email, firstName, secondName, password);
        List<String> errorMessages = ValidationHelper.defineValidationErrorMessages(validationResult);
        List<String> validParameters = ValidationHelper.defineValidParameters(validationResult);
        return Map.entry(validParameters, errorMessages);
    }

    @Override
    public Map.Entry<List<String>, List<String>> validateLoginData(String login, String password) {
        Map<String, Boolean> validationResult = UserValidator.validateLoginData(login, password);
        List<String> errorMessages = ValidationHelper.defineValidationErrorMessages(validationResult);
        List<String> validParameters = ValidationHelper.defineValidParameters(validationResult);
        return Map.entry(validParameters, errorMessages);
    }

    private void setDefaultFields(User user) {
        user.setPicture(DEFAULT_USER_PICTURE);
        user.setRating(0);
        user.setRole(UserRole.USER);
        user.setState(UserState.INACTIVE);
    }
}
