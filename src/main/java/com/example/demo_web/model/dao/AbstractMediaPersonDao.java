package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public abstract class AbstractMediaPersonDao extends AbstractBaseDao<Integer, MediaPerson> {
    public abstract List<MediaPerson> findAllBetween(int begin, int end) throws DaoException;
    public abstract int countMediaPersons() throws DaoException;
    public abstract List<MediaPerson> findByMovieId(Integer id) throws DaoException;
    public abstract boolean insertMediaPersonMovie(Integer mediaPersonId, Integer movieId) throws DaoException;
    public abstract boolean deleteMediaPersonMovies(Integer mediaPersonId) throws DaoException;
    public abstract boolean idExists(int id) throws DaoException;
}
