package com.epam.project.model.dao;

import com.epam.project.exception.ConnectionException;
import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserRole;
import com.epam.project.model.entity.UserState;
import com.epam.project.model.pool.ConnectionPool;
import org.testng.Assert;
import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class UserDaoTest {
    private AbstractUserDao userDao;
    private EntityTransaction transaction;
    private User user;

    @BeforeClass
    public void initialize() {
        userDao = UserDao.getInstance();
        transaction = new EntityTransaction();
        transaction.init(userDao);
        user = new User();
        user.setId(0);
        user.setLogin("login");
        user.setEmail("user@gmail.com");
        user.setFirstName("name");
        user.setSecondName("name");
        user.setPicture("picture.jpg");
        user.setRole(UserRole.USER);
        user.setState(UserState.ACTIVE);
        user.setRating(0);
    }

    @BeforeMethod
    public void initializeTest() throws DaoException {
        user = userDao.create(user, "12345678");
    }

    @Test
    public void testFindEntityById() throws DaoException {
        User actualResult = userDao.findEntityById(user.getId());
        assertEquals(user, actualResult);
    }

    @Test
    public void testDetectStateById() throws DaoException {
        UserState actualResult = userDao.detectStateById(user.getId());
        assertEquals(user.getState(), actualResult);
    }

    @Test
    public void testFindUserByLogin() throws DaoException {
        User actualResult = userDao.findUserByLogin(user.getLogin());
        assertEquals(user, actualResult);
    }

    @Test
    public void testLoginExists() throws DaoException {
        boolean state = userDao.loginExists(user.getLogin());
        assertTrue(state);
    }

    @Test
    public void testBlockUser() throws DaoException {
        userDao.blockUser(user.getId());
        User foundUser = userDao.findEntityById(user.getId());
        UserState actual = foundUser.getState();
        assertEquals(UserState.BLOCKED, actual);
    }

    @Test
    public void testActivateUser() throws DaoException {
        userDao.blockUser(user.getId());
        userDao.activateUser(user.getId());
        User foundUser = userDao.findEntityById(user.getId());
        UserState actual = foundUser.getState();
        assertEquals(UserState.ACTIVE, actual);
    }

    @Test
    public void testIdExists() throws DaoException {
        boolean state = userDao.idExists(user.getId());
        assertTrue(state);
    }

    @Test
    public void testFindRoleById() throws DaoException {
        User foundUser = userDao.findEntityById(user.getId());
        UserRole actual = foundUser.getRole();
        assertEquals(UserRole.USER, actual);
    }

    @AfterMethod
    public void endTest() throws DaoException {
        userDao.delete(user.getId());
    }

    @AfterClass
    public void clear() {
        transaction.end();
        transaction = null;
        user = null;
        userDao = null;
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            Assert.fail(e.getMessage());
        }
    }
}