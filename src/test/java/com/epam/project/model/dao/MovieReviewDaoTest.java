package com.epam.project.model.dao;

import com.epam.project.exception.ConnectionException;
import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.MovieReview;
import com.epam.project.model.pool.ConnectionPool;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class MovieReviewDaoTest {
    private AbstractMovieReviewDao movieReviewDao;
    private EntityTransaction transaction;
    private MovieReview review;

    @BeforeClass
    public void initialize() {
        movieReviewDao = MovieReviewDao.getInstance();
        transaction = new EntityTransaction();
        transaction.init(movieReviewDao);
        review = new MovieReview();
        review.setId(0);
        review.setTitle("title");
        review.setBody("body");
        review.setCreationDate(LocalDate.now());
        review.setMovieId(50);
        review.setUserId(1);
    }

    @BeforeMethod
    public void initializeTest() throws DaoException {
        review = movieReviewDao.create(review);
    }

    @Test
    public void testFindEntityById() throws DaoException {
        MovieReview result = movieReviewDao.findEntityById(review.getId());
        assertEquals(review, result);
    }

    @Test
    public void testIsUnique() throws DaoException {
        boolean state = movieReviewDao.isUnique(review.getMovieId(), review.getUserId());
        assertFalse(state);
    }

    @Test
    public void testExists() throws DaoException {
        boolean state = movieReviewDao.exists(review.getId(), review.getMovieId(), review.getUserId());
        assertTrue(state);
    }

    @AfterMethod
    public void endTest() throws DaoException {
        movieReviewDao.delete(review.getId());
    }

    @AfterClass
    public void clear() {
        transaction.end();
        transaction = null;
        review = null;
        movieReviewDao = null;
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            Assert.fail(e.getMessage());
        }
    }
}