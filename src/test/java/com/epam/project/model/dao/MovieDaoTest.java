package com.epam.project.model.dao;

import com.epam.project.exception.ConnectionException;
import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.Movie;
import com.epam.project.model.pool.ConnectionPool;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class MovieDaoTest {
    private AbstractMovieDao movieDao;
    private EntityTransaction transaction;
    private Movie movie;

    @BeforeClass
    public void initialize() {
        movieDao = MovieDao.getInstance();
        transaction = new EntityTransaction();
        transaction.init(movieDao);
        movie = new Movie();
        movie.setId(0);
        movie.setTitle("title");
        movie.setDescription("description");
        movie.setAverageRating(0);
        movie.setPicture("movie.gif");
        movie.setReleaseDate(LocalDate.now());
    }

    @BeforeMethod
    public void initializeTest() throws DaoException {
        movie = movieDao.create(movie);
    }

    @Test
    public void testIdExists() throws DaoException {
        boolean state = movieDao.idExists(movie.getId());
        assertTrue(state);
    }

    @Test
    public void testFindEntityById() throws DaoException {
        Movie result = movieDao.findEntityById(movie.getId());
        assertEquals(movie, result);
    }

    @Test
    public void testFindRatingById() throws DaoException {
        float expected = movie.getAverageRating();
        float actual = movieDao.findRatingById(movie.getId());
        assertEquals(actual, expected);
    }

    @Test
    public void testIsUnique() throws DaoException {
        boolean state = movieDao.isUnique(movie.getTitle(), movie.getReleaseDate());
        assertFalse(state);
    }

    @AfterMethod
    public void endTest() throws DaoException {
        movieDao.delete(movie.getId());
    }

    @AfterClass
    public void clear() {
        transaction.end();
        transaction = null;
        movie = null;
        movieDao = null;
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            Assert.fail(e.getMessage());
        }
    }
}