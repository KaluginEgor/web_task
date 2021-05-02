package com.epam.project.model.dao;

import com.epam.project.exception.ConnectionException;
import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.MovieRating;
import com.epam.project.model.pool.ConnectionPool;
import org.testng.Assert;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class MovieRatingDaoTest {
    private AbstractMovieRatingDao movieRatingDao;
    private EntityTransaction transaction;
    private MovieRating rating;

    @BeforeClass
    public void initialize() {
        movieRatingDao = MovieRatingDao.getInstance();
        transaction = new EntityTransaction();
        transaction.init(movieRatingDao);
        rating = new MovieRating();
        rating.setId(0);
        rating.setValue(5);
        rating.setUserId(1);
        rating.setMovieId(50);
    }

    @BeforeMethod
    public void initializeTest() throws DaoException {
        rating = movieRatingDao.create(rating);
    }

    @Test
    public void testFindEntityById() throws DaoException {
        MovieRating result = movieRatingDao.findEntityById(rating.getId());
        assertEquals(result, result);
    }

    @Test
    public void testExists() throws DaoException {
        boolean state = movieRatingDao.exists(rating.getId(), rating.getMovieId(), rating.getUserId());
        assertTrue(state);
    }

    @Test
    public void testIsUnique() throws DaoException {
        boolean state = movieRatingDao.isUnique(rating.getMovieId(), rating.getUserId());
        assertFalse(state);
    }

    @AfterMethod
    public void endTest() throws DaoException {
        movieRatingDao.delete(rating.getId());
    }

    @AfterClass
    public void clear() {
        transaction.end();
        transaction = null;
        rating = null;
        movieRatingDao = null;
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            Assert.fail(e.getMessage());
        }
    }
}