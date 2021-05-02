package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.MovieReview;

import java.util.List;

/**
 * The type Abstract movie review dao.
 */
public abstract class AbstractMovieReviewDao extends AbstractBaseDao<Integer, MovieReview> {
    /**
     * Find by movie id list.
     *
     * @param id the id
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<MovieReview> findByMovieId(Integer id) throws DaoException;

    /**
     * Find by user id list.
     *
     * @param id the id
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<MovieReview> findByUserId(Integer id) throws DaoException;

    /**
     * Is unique boolean.
     *
     * @param movieId the movie id
     * @param userId  the user id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean isUnique(int movieId, int userId) throws DaoException;

    /**
     * Exists boolean.
     *
     * @param reviewId the review id
     * @param movieId  the movie id
     * @param userId   the user id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean exists(int reviewId, int movieId, int userId) throws DaoException;
}
