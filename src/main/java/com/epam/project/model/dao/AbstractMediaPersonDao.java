package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.MediaPerson;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public abstract class AbstractMediaPersonDao extends AbstractBaseDao<Integer, MediaPerson> {
    public abstract List<MediaPerson> findAllBetween(int begin, int end) throws DaoException;
    public abstract Map<Integer, String> findAllNames() throws DaoException;
    public abstract int countMediaPersons() throws DaoException;
    public abstract List<MediaPerson> findByMovieId(Integer id) throws DaoException;
    public abstract boolean insertMediaPersonMovie(Integer mediaPersonId, Integer movieId) throws DaoException;
    public abstract boolean deleteMediaPersonMovies(Integer mediaPersonId) throws DaoException;
    public abstract boolean idExists(int id) throws DaoException;
    public abstract boolean isUnique(String firstName, String secondName, LocalDate birthday) throws DaoException;
}
