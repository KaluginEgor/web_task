package com.example.demo_web.service.impl;

import com.example.demo_web.dao.MovieDao;
import com.example.demo_web.dao.impl.MovieDaoImpl;
import com.example.demo_web.entity.Movie;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.service.MovieService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceImpl implements MovieService {
    private static final Logger logger = LogManager.getLogger(MovieServiceImpl.class);
    private MovieDao movieDao = MovieDaoImpl.getInstance();

    @Override
    public List<Movie> findAll(int begin, int end) throws ServiceException {
        List<Movie> allUsers;
        try {
            allUsers = new ArrayList<>(movieDao.findAllBetween(begin, end));
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
        return allUsers;
    }

    @Override
    public int countMovies() throws ServiceException {
        int moviesCount;
        try {
            moviesCount = movieDao.countMovies();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
        return moviesCount;
    }
}
