package com.example.demo_web.service.impl;

import com.example.demo_web.builder.UserBuilder;
import com.example.demo_web.builder.impl.UserBuilderImpl;
import com.example.demo_web.command.ErrorMessage;
import com.example.demo_web.command.RequestParameter;
import com.example.demo_web.command.SessionRequestContent;
import com.example.demo_web.connection.ConnectionPool;
import com.example.demo_web.dao.api.UserDao;
import com.example.demo_web.dao.api.impl.UserDaoImpl;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.mail.MailBuilder;
import com.example.demo_web.mail.MailSender;
import com.example.demo_web.service.UserService;
import com.example.demo_web.validator.UserValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
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
    private final UserDao userDao = UserDaoImpl.getInstance();

    public UserServiceImpl() {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
        } catch (ConnectionException e) {
            logger.error(e);
        }
        userDao.setConnection(connection);
    }

    @Override
    public Optional<User> login(String login, String password) throws ServiceException {
        Optional<User> foundUser = Optional.empty();
        try {
            String encryptedPassword = DigestUtils.md5Hex(password);
            if (userDao.loginExists(login)) {
                Optional<String> passwordFromDB = userDao.findPasswordByLogin(login);
                if (passwordFromDB.isPresent()) {
                    if (passwordFromDB.get().equals(encryptedPassword)) {
                        foundUser = userDao.findUserByLogin(login);
                    }
                }
            }
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
            UserBuilder<User> builder = new UserBuilderImpl();
            builder.setDefaultFields(user);
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
    public void constructAndSendConfirmEmail(String locale, User user) {
        String emailSubject = MailBuilder.buildEmailSubject(locale);
        String emailBody = MailBuilder.buildEmailBody(user,locale);
        MailSender mailSender = new MailSender(emailSubject, emailBody, user.getEmail());
        mailSender.send();
    }

    @Override
    public boolean isValid(String login, String email, String firstName, String secondName, String password) throws ServiceException {
        return UserValidator.isValidLogin(login) && UserValidator.isValidEmail(email)
                && UserValidator.isValidName(firstName) && UserValidator.isValidName(secondName)
                && UserValidator.isValidPassword(password);
    }

    @Override
    public Map<String, Boolean> defineIncorrectLoginData(String login, String password) throws ServiceException {
        Map<String, Boolean> validatedUsersData = UserValidator.validateData(login, password);
        try {
            validatedUsersData.put(LOGIN_EXISTS, userDao.loginExists(login));
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

    public void defineErrorMessageFromRegistrationDataValidations(SessionRequestContent sessionRequestContent,
                                                                  Map<String, Boolean> usersDataValidations) {
        String falseKey = defineFalseKey(usersDataValidations);
        switch (falseKey) {
            case LOGIN:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.LOGIN_INCORRECT_ERROR_MESSAGE);
                break;
            case EMAIL:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.EMAIL_INCORRECT_ERROR_MESSAGE);
                break;
            case FIRST_NAME:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.FIRST_NAME_INCORRECT_ERROR_MESSAGE);
                break;
            case SECOND_NAME:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.SECOND_NAME_INCORRECT_ERROR_MESSAGE);
                break;
            case PASSWORD:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.PASSWORD_INCORRECT_ERROR_MESSAGE);
                break;
            case LOGIN_IS_UNIQUE:
            sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.LOGIN_IS_NOT_UNIQUE_ERROR_MESSAGE);
                break;
            case LOGIN_EXISTS:
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE,
                        ErrorMessage.LOGIN_DOESNT_EXIST_ERROR_MESSAGE);
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
}
