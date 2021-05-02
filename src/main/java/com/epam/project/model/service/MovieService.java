package com.epam.project.model.service;

import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.Movie;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface Movie service.
 */
public interface MovieService {
    /**
     * Find all between list.
     *
     * @param begin the begin
     * @param end   the end
     * @return the list
     * @throws ServiceException the service exception
     */
    List<Movie> findAllBetween(int begin, int end) throws ServiceException;

    /**
     * Count movies int.
     *
     * @return the int
     * @throws ServiceException the service exception
     */
    int countMovies() throws ServiceException;

    /**
     * Find by id map . entry.
     *
     * @param stringMovieId the string movie id
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<Movie>, Optional<String>> findById(String stringMovieId) throws ServiceException;

    /**
     * Find all titles map.
     *
     * @return the map
     * @throws ServiceException the service exception
     */
    Map<Integer, String> findAllTitles() throws ServiceException;

    /**
     * Id exists boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws ServiceException the service exception
     */
    boolean idExists(int id) throws ServiceException;

    /**
     * Create map . entry.
     *
     * @param title                the title
     * @param description          the description
     * @param stringReleaseDate    the string release date
     * @param picture              the picture
     * @param stringGenres         the string genres
     * @param stringMediaPersonsId the string media persons id
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<Movie>, Optional<String>> create(String title, String description, String stringReleaseDate,
                                                        String picture, String[] stringGenres,
                                                        String[] stringMediaPersonsId) throws ServiceException;

    /**
     * Update map . entry.
     *
     * @param stringId             the string id
     * @param title                the title
     * @param description          the description
     * @param releaseDate          the release date
     * @param picture              the picture
     * @param stringGenres         the string genres
     * @param stringMediaPersonsId the string media persons id
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<Movie>, Optional<String>> update(String stringId, String title, String description, String releaseDate,
                                                        String picture, String[] stringGenres,
                                                        String[] stringMediaPersonsId) throws ServiceException;

    /**
     * Delete optional.
     *
     * @param stringId the string id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> delete(String stringId) throws ServiceException;

    /**
     * Find by name part map . entry.
     *
     * @param title the title
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<List<Movie>, Optional<String>> findByNamePart(String title) throws ServiceException;

    /**
     * Validate data map . entry.
     *
     * @param title                the title
     * @param description          the description
     * @param stringReleaseDate    the string release date
     * @param picture              the picture
     * @param stringGenresId       the string genres id
     * @param stringMediaPersonsId the string media persons id
     * @return the map . entry
     */
    Map.Entry<List<String>, List<String>> validateData(String title, String description, String stringReleaseDate, String picture,
                                                       String[] stringGenresId, String[] stringMediaPersonsId);
}