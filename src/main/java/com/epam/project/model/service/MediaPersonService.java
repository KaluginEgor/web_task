package com.epam.project.model.service;

import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.MediaPerson;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface Media person service.
 */
public interface MediaPersonService {
    /**
     * Find all between list.
     *
     * @param begin the begin
     * @param end   the end
     * @return the list
     * @throws ServiceException the service exception
     */
    List<MediaPerson> findAllBetween(int begin, int end) throws ServiceException;

    /**
     * Fina all names map.
     *
     * @return the map
     * @throws ServiceException the service exception
     */
    Map<Integer, String> finaAllNames() throws ServiceException;

    /**
     * Count media persons int.
     *
     * @return the int
     * @throws ServiceException the service exception
     */
    int countMediaPersons() throws ServiceException;

    /**
     * Id exists boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws ServiceException the service exception
     */
    boolean idExists(int id) throws ServiceException;

    /**
     * Find by id map . entry.
     *
     * @param stringMediaPersonId the string media person id
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<MediaPerson>, Optional<String>> findById(String stringMediaPersonId) throws ServiceException;

    /**
     * Update map . entry.
     *
     * @param stringId           the string id
     * @param firstName          the first name
     * @param secondName         the second name
     * @param bio                the bio
     * @param stringOccupationId the string occupation id
     * @param stringBirthday     the string birthday
     * @param picture            the picture
     * @param stringMoviesId     the string movies id
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<MediaPerson>, Optional<String>> update(String stringId, String firstName, String secondName,
                                                              String bio, String stringOccupationId, String stringBirthday,
                                                              String picture, String[] stringMoviesId) throws ServiceException;

    /**
     * Create map . entry.
     *
     * @param firstName          the first name
     * @param secondName         the second name
     * @param bio                the bio
     * @param stringOccupationId the string occupation id
     * @param stringBirthday     the string birthday
     * @param picture            the picture
     * @param stringMoviesId     the string movies id
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<MediaPerson>, Optional<String>> create(String firstName, String secondName,
                                                              String bio, String stringOccupationId, String stringBirthday,
                                                              String picture, String[] stringMoviesId) throws ServiceException;

    /**
     * Delete optional.
     *
     * @param stringId the string id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> delete(String stringId) throws ServiceException;

    /**
     * Validate data map . entry.
     *
     * @param firstName          the first name
     * @param secondName         the second name
     * @param bio                the bio
     * @param stringOccupationId the string occupation id
     * @param stringBirthday     the string birthday
     * @param picture            the picture
     * @param stringMoviesId     the string movies id
     * @return the map . entry
     */
    Map.Entry<List<String>, List<String>> validateData(String firstName, String secondName, String bio, String stringOccupationId,
                                                       String stringBirthday, String picture, String[] stringMoviesId);
}
