package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.GenreType;
import com.epam.project.model.entity.Movie;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The type Abstract movie dao.
 */
public abstract class AbstractMovieDao extends AbstractBaseDao<Integer, Movie> {
    /**
     * Find all between list.
     *
     * @param begin the begin
     * @param end   the end
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<Movie> findAllBetween(int begin, int end) throws DaoException;

    /**
     * Find all titles map.
     *
     * @return the map
     * @throws DaoException the dao exception
     */
    public abstract Map<Integer, String> findAllTitles() throws DaoException;

    /**
     * Count movies int.
     *
     * @return the int
     * @throws DaoException the dao exception
     */
    public abstract int countMovies() throws DaoException;

    /**
     * Find genre types by movie id list.
     *
     * @param id the id
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<GenreType> findGenreTypesByMovieId(Integer id) throws DaoException;

    /**
     * Find by actor id list.
     *
     * @param id the id
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<Movie> findByActorId(Integer id) throws DaoException;

    /**
     * Insert movie genre boolean.
     *
     * @param movieId the movie id
     * @param genreId the genre id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean insertMovieGenre(Integer movieId, Integer genreId) throws DaoException;

    /**
     * Insert movie media person boolean.
     *
     * @param movieId       the movie id
     * @param mediaPersonId the media person id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean insertMovieMediaPerson(Integer movieId, Integer mediaPersonId) throws DaoException;

    /**
     * Delete movie crew boolean.
     *
     * @param movieId the movie id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean deleteMovieCrew(Integer movieId) throws DaoException;

    /**
     * Delete movie genres boolean.
     *
     * @param movieId the movie id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean deleteMovieGenres(Integer movieId) throws DaoException;

    /**
     * Find by title part list.
     *
     * @param movieTitle the movie title
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<Movie> findByTitlePart(String movieTitle) throws DaoException;

    /**
     * Find rating by id float.
     *
     * @param movieId the movie id
     * @return the float
     * @throws DaoException the dao exception
     */
    public abstract float findRatingById(Integer movieId) throws DaoException;

    /**
     * Update rating by id boolean.
     *
     * @param rating  the rating
     * @param movieId the movie id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean updateRatingById(float rating, Integer movieId) throws DaoException;

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
     * @param title       the title
     * @param releaseDate the release date
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean isUnique(String title, LocalDate releaseDate) throws DaoException;
}
