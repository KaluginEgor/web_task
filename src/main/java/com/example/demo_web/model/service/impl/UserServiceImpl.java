package com.example.demo_web.model.service.impl;

import com.example.demo_web.controller.command.ErrorMessage;
import com.example.demo_web.controller.command.RequestParameter;
import com.example.demo_web.controller.command.SessionRequestContent;
import com.example.demo_web.model.dao.UserDao;
import com.example.demo_web.model.dao.impl.UserDaoImpl;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.entity.UserRole;
import com.example.demo_web.model.entity.UserState;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.mail.MailBuilder;
import com.example.demo_web.mail.MailSender;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.validator.UserValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private static final String LOGIN_IS_UNIQUE = "loginIsUnique";
    private static final String LOGIN_EXISTS = "loginExists";
    private static final String LOGIN = "login";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "firstName";
    private static final String SECOND_NAME = "secondName";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_IS_CORRECT = "passwordIsCorrect";
    private static final String USER_NOT_INACTIVE = "userNotInactive";
    private static final String USER_NOT_BLOCKED = "userNotBlocked";
    private static final String USER_NOT_DELETED = "userNotDeleted";

    private final UserDao userDao = UserDaoImpl.getInstance();

    public UserServiceImpl() {
    }

    @Override
    public Optional<User> login(String login, String password) throws ServiceException {
        Optional<User> foundUser = Optional.empty();
        try {
            foundUser = userDao.findUserByLogin(login);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return foundUser;
    }

    @Override
    public Optional<User> register(String login, String email, String firstName, String secondName, String password) throws ServiceException {
        Optional<User> registeredUser = Optional.empty();
        User user = new User(login, email, firstName, secondName);

        try {
            String encryptedPassword = DigestUtils.md5Hex(password);
            setDefaultFields(user);
            int userId = userDao.create(user, encryptedPassword);
            user.setId(userId);
            registeredUser = Optional.of(user);
            logger.info("User {} created.", registeredUser);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return registeredUser;
    }

    @Override
    public boolean activateUser(int id) throws ServiceException {
        try {
            return userDao.activateUser(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void constructAndSendConfirmEmail(String locale, User user) {
        String emailSubject = MailBuilder.buildEmailSubject(locale);
        String emailBody = MailBuilder.buildEmailBody(user,locale);
        MailSender mailSender = new MailSender(emailSubject, emailBody, user.getEmail());
        mailSender.send();
    }

    @Override
    public Map<String, Boolean> defineIncorrectLoginData(String login, String password) throws ServiceException {
        Map<String, Boolean> validatedUsersData = UserValidator.validateData(login, password);
        try {
            validatedUsersData.put(LOGIN_EXISTS, userDao.loginExists(login));

            if (userDao.loginExists(login)) {
                String encryptedPassword = DigestUtils.md5Hex(password);
                Optional<String> passwordFromDB = userDao.findPasswordByLogin(login);
                boolean correctPassword = passwordFromDB.get().equals(encryptedPassword);
                validatedUsersData.put(PASSWORD_IS_CORRECT, correctPassword);

                if (correctPassword) {
                    Optional<User> user = userDao.findUserByLogin(login);
                    UserState userState = user.get().getState();
                    validatedUsersData.put(USER_NOT_INACTIVE, !UserState.INACTIVE.equals(userState));
                    validatedUsersData.put(USER_NOT_BLOCKED, !UserState.BLOCKED.equals(userState));
                    validatedUsersData.put(USER_NOT_DELETED, !UserState.DELETED.equals(userState));
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return validatedUsersData;
    }

    @Override
    public Map<String, Boolean> defineIncorrectRegistrationData(String login, String email, String firstName, String secondName, String password) throws ServiceException {
        Map<String, Boolean> validatedUsersData = UserValidator.validateData(login, email, firstName, secondName, password);
        try {
            validatedUsersData.put(LOGIN_IS_UNIQUE, userDao.findUserByLogin(login).isEmpty());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return validatedUsersData;
    }

    public void defineErrorMessageFromDataValidations(SessionRequestContent sessionRequestContent,
                                                      Map<String, Boolean> usersDataValidations) {
        String falseKey = defineFalseKey(usersDataValidations);
        switch (falseKey) {
            case LOGIN:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.LOGIN_INCORRECT_ERROR_MESSAGE);
                sessionRequestContent.removeRequestAttribute(LOGIN);
                break;
            case EMAIL:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.EMAIL_INCORRECT_ERROR_MESSAGE);
                sessionRequestContent.removeRequestAttribute(EMAIL);
                break;
            case FIRST_NAME:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.FIRST_NAME_INCORRECT_ERROR_MESSAGE);
                sessionRequestContent.removeRequestAttribute(FIRST_NAME);
                break;
            case SECOND_NAME:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.SECOND_NAME_INCORRECT_ERROR_MESSAGE);
                sessionRequestContent.removeRequestAttribute(SECOND_NAME);
                break;
            case PASSWORD:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.PASSWORD_INCORRECT_ERROR_MESSAGE);
                sessionRequestContent.removeRequestAttribute(PASSWORD);
                break;
            case LOGIN_IS_UNIQUE:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                            ErrorMessage.LOGIN_IS_NOT_UNIQUE_ERROR_MESSAGE);
                sessionRequestContent.removeRequestAttribute(LOGIN);
                break;
            case LOGIN_EXISTS:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.LOGIN_DOESNT_EXIST_ERROR_MESSAGE);
                sessionRequestContent.removeRequestAttribute(LOGIN);
                break;
            case PASSWORD_IS_CORRECT:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.ENTERED_PASSWORD_INCORRECT_ERROR_MESSAGE);
                break;
            case USER_NOT_INACTIVE:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.USER_INACTIVE_ERROR_MESSAGE);
                break;
            case USER_NOT_BLOCKED:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.USER_BLOCKED_ERROR_MESSAGE);
                break;
            case USER_NOT_DELETED:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.USER_DELETED_ERROR_MESSAGE);
                break;
        }
    }

    private static String defineFalseKey(Map<String, Boolean> map) {
        Optional<String> falseKey = map.entrySet()
                .stream()
                .filter(entry -> Boolean.FALSE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
        return falseKey.get();
    }

    private void setDefaultFields(User user) {
        user.setRole(UserRole.USER);
        user.setState(UserState.INACTIVE);
    }
}
