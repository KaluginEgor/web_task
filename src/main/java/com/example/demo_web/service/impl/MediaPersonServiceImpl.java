package com.example.demo_web.service.impl;

import com.example.demo_web.dao.MediaPersonDao;
import com.example.demo_web.dao.MovieDao;
import com.example.demo_web.dao.impl.MediaPersonDaoImpl;
import com.example.demo_web.dao.impl.MovieDaoImpl;
import com.example.demo_web.entity.*;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.service.MediaPersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MediaPersonServiceImpl implements MediaPersonService {
    private static final Logger logger = LogManager.getLogger(MediaPersonServiceImpl.class);
    private MediaPersonDao mediaPersonDao = MediaPersonDaoImpl.getInstance();
    private MovieDao movieDao = MovieDaoImpl.getInstance();

    @Override
    public List<MediaPerson> findAll(int begin, int end) throws ServiceException {
        List<MediaPerson> allUsers;
        try {
            allUsers = new ArrayList<>(mediaPersonDao.findAllBetween(begin, end));
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
        return allUsers;
    }

    @Override
    public int countMediaPersons() throws ServiceException {
        int moviesCount;
        try {
            moviesCount = mediaPersonDao.countMediaPersons();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
        return moviesCount;
    }

    @Override
    public MediaPerson findById(Integer id) throws ServiceException {
        MediaPerson mediaPerson = null;
        try {
            mediaPerson = mediaPersonDao.findEntityById(id);
            List<Movie> movies = movieDao.findByActorId(id);
            mediaPerson.setMovies(movies);
            return mediaPerson;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public MediaPerson update(MediaPerson mediaPerson) throws ServiceException {
        try {
            return mediaPersonDao.update(mediaPerson);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
