package com.epam.project.model.service.impl;

import com.epam.project.exception.ConnectionException;
import com.epam.project.exception.DaoException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.dao.AbstractMovieDao;
import com.epam.project.model.entity.Movie;
import com.epam.project.model.pool.ConnectionPool;
import com.epam.project.model.service.MovieService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

public class MovieServiceImplTest {
    @Mock
    private AbstractMovieDao movieDao;
    private MovieService movieService;
    private Movie movie1;
    private Movie movie2;

    @BeforeClass
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        movieService = MovieServiceImpl.getInstance();
        movie1 = new Movie();
        movie1.setId(60);
        movie1.setTitle("Aliens");
        movie1.setDescription("Fifty-seven years after surviving an apocalyptic attack aboard her space vessel by merciless space creatures, " +
                "Officer Ripley awakens from hyper-sleep and tries to warn anyone who will listen about the predators.");
        movie1.setAverageRating(10);
        movie1.setPicture("C:/Epam/pictures/1f4d6054-0891-4146-837e-c5e673087375.png");
        movie1.setReleaseDate(LocalDate.parse("1986-07-18"));
        movie2 = new Movie();
        movie2.setId(50);
        movie2.setTitle("Avatar");
        movie2.setDescription("When his brother is killed in a robbery, paraplegic Marine Jake Sully decides to take his place in a mission " +
                "on the distant world of Pandora. There he learns of greedy corporate figurehead Parker Selfridge's intentions of driving off " +
                "the native humanoid \"Na'vi\" in order to mine for the precious material scattered throughout their rich woodland. In exchange " +
                "for the spinal surgery that will fix his legs, Jake gathers knowledge, of the Indigenous Race and their Culture, for the cooperating " +
                "military unit spearheaded by gung-ho Colonel Quaritch, while simultaneously attempting to infiltrate the Na'vi people with the use of an " +
                "\"avatar\" identity. While Jake begins to bond with the native tribe and quickly falls in love with the beautiful alien Neytiri, the restless " +
                "Colonel moves forward with his ruthless extermination tactics, forcing the soldier to take a stand - and fight back in an epic battle for the fate of Pandora.");
        movie2.setAverageRating(0);
        movie2.setPicture("C:/Epam/pictures/a99e7817-fc59-4086-8619-2c9a95782206.jpg");
        movie2.setReleaseDate(LocalDate.parse("2009-12-18"));
    }

    @Test
    public void testFindAllBetween() throws DaoException, ServiceException {
        int start = 0;
        int end = 2;
        List<Movie> expected = List.of(movie1, movie2);
        given(movieDao.findAllBetween(start, end)).willReturn(expected);
        List<Movie> actual = movieService.findAllBetween(start, end);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testFindById() throws DaoException, ServiceException {
        int movieId = 50;
        String stringId = "50";
        given(movieDao.findEntityById(movieId)).willReturn(movie2);
        Optional<Movie> actual = movieService.findById(stringId).getKey();
        assertEquals(actual.get(), movie2);
    }

    @AfterClass
    public void clear() {
        movieService = null;
        movie2 = null;
        movie2 = null;
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            Assert.fail(e.getMessage());
        }
    }
}