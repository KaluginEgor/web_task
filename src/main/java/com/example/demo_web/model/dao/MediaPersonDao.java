package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public interface MediaPersonDao extends BaseDao<Integer, MediaPerson> {
    List<MediaPerson> findAllBetween(int begin, int end) throws DaoException;
    int countMediaPersons() throws DaoException;
    List<MediaPerson> findByMovieId(Integer id) throws DaoException;
    boolean insertMediaPersonMovie(Integer mediaPersonId, Integer movieId) throws DaoException;
    boolean deleteMediaPersonMovies(Integer mediaPersonId) throws DaoException;
}
