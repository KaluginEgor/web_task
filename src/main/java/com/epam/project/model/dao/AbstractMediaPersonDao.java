package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.MediaPerson;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The type Abstract media person dao.
 */
public abstract class AbstractMediaPersonDao extends AbstractBaseDao<Integer, MediaPerson> {
    /**
     * Find all between list.
     *
     * @param begin the begin
     * @param end   the end
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<MediaPerson> findAllBetween(int begin, int end) throws DaoException;

    /**
     * Find all names map.
     *
     * @return the map
     * @throws DaoException the dao exception
     */
    public abstract Map<Integer, String> findAllNames() throws DaoException;

    /**
     * Count media persons int.
     *
     * @return the int
     * @throws DaoException the dao exception
     */
    public abstract int countMediaPersons() throws DaoException;

    /**
     * Find by movie id list.
     *
     * @param id the id
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<MediaPerson> findByMovieId(Integer id) throws DaoException;

    /**
     * Insert media person movie boolean.
     *
     * @param mediaPersonId the media person id
     * @param movieId       the movie id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean insertMediaPersonMovie(Integer mediaPersonId, Integer movieId) throws DaoException;

    /**
     * Delete media person movies boolean.
     *
     * @param mediaPersonId the media person id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean deleteMediaPersonMovies(Integer mediaPersonId) throws DaoException;

    /**
     * Id exists boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean idExists(int id) throws DaoException;

    /**
     * Is unique boolean.
     *
     * @param firstName  the first name
     * @param secondName the second name
     * @param birthday   the birthday
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean isUnique(String firstName, String secondName, LocalDate birthday) throws DaoException;
}
