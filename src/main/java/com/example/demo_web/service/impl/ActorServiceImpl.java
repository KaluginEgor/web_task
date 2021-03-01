package com.example.demo_web.service.impl;

import com.example.demo_web.dao.ActorDao;
import com.example.demo_web.dao.MovieDao;
import com.example.demo_web.dao.impl.ActorDaoImpl;
import com.example.demo_web.dao.impl.MovieDaoImpl;
import com.example.demo_web.entity.Actor;
import com.example.demo_web.entity.Movie;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.service.ActorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ActorServiceImpl implements ActorService {
    private static final Logger logger = LogManager.getLogger(ActorServiceImpl.class);
    private ActorDao actorDao = ActorDaoImpl.getInstance();

    @Override
    public List<Actor> findAll(int begin, int end) throws ServiceException {
        List<Actor> allUsers;
        try {
            allUsers = new ArrayList<>(actorDao.findAllBetween(begin, end));
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
        return allUsers;
    }

    @Override
    public int countActors() throws ServiceException {
        int moviesCount;
        try {
            moviesCount = actorDao.countActors();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
        return moviesCount;
    }
}
