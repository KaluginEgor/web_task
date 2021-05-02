package com.epam.project.model.service.impl;

import com.epam.project.exception.ConnectionException;
import com.epam.project.exception.DaoException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.dao.AbstractUserDao;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserRole;
import com.epam.project.model.entity.UserState;
import com.epam.project.model.pool.ConnectionPool;
import com.epam.project.model.service.UserService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

public class UserServiceImplTest {
    @Mock
    private AbstractUserDao userDao;
    private UserService userService;
    private User admin;
    private User user;

    @BeforeClass
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        userService = UserServiceImpl.getInstance();
        admin = new User();
        admin.setId(1);
        admin.setLogin("admin");
        admin.setFirstName("ADMIN");
        admin.setSecondName("ADMIN");
        admin.setEmail("admin@gmail.com");
        admin.setRole(UserRole.ADMIN);
        admin.setState(UserState.ACTIVE);
        admin.setPicture("C:/Epam/pictures/8e581676-ed01-453d-96c1-0b5037f56ade.jpg");
        user = new User();
        user.setId(25);
        user.setLogin("login1");
        user.setFirstName("firstname");
        user.setSecondName("secondname");
        user.setEmail("email1@gmail.com");
        user.setRole(UserRole.USER);
        user.setState(UserState.ACTIVE);
        user.setPicture("1.png");
    }

    @Test
    public void testDetectStateById() throws DaoException, ServiceException {
        int userId = 1;
        UserState expected = UserState.ACTIVE;
        given(userDao.detectStateById(userId)).willReturn(expected);
        UserState actual = userService.detectStateById(userId);
        assertEquals(actual, expected);
    }

    @Test
    public void testFindAllBetween() throws DaoException, ServiceException {
        int start = 0;
        int end = 2;
        List<User> expected = List.of(admin, user);
        given(userDao.findAllBetween(start, end)).willReturn(expected);
        List<User> actual = userService.findAllBetween(start, end);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testFindById() throws DaoException, ServiceException {
        int userId = 1;
        String stringId = "1";
        given(userDao.findEntityById(userId)).willReturn(admin);
        Optional<User> actualUser = userService.findById(stringId).getKey();
        assertEquals(actualUser.get(), admin);
    }

    @AfterClass
    public void clear() {
        userService = null;
        admin = null;
        user = null;
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            Assert.fail(e.getMessage());
        }
    }
}