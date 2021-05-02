package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.MovieRating;

import java.util.List;

/**
 * The type Abstract movie rating dao.
 */
public abstract class AbstractMovieRatingDao extends AbstractBaseDao<Integer, MovieRating>{
    /**
     * Find by movie id list.
     *
     * @param id the id
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<MovieRating> findByMovieId(Integer id) throws DaoException;

    /**
     * Find by user id list.
     *
     * @param id the id
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<MovieRating> findByUserId(Integer id) throws DaoException;

    /**
     * Count ratings by movie id int.
     *
     * @param movieId the movie id
     * @return the int
     * @throws DaoException the dao exception
     */
    public abstract int countRatingsByMovieId(Integer movieId) throws DaoException;

    /**
     * Exists boolean.
     *
     * @param ratingId the rating id
     * @param movieId  the movie id
     * @param userId   the user id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean exists(int ratingId, int movieId, int userId) throws DaoException;

    /**
     * Is unique boolean.
     *
     * @param movieId the movie id
     * @param userId  the user id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean isUnique(int movieId, int userId) throws DaoException;
}
