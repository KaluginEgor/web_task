package com.epam.project.model.service;

import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.MediaPerson;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MediaPersonService {
    List<MediaPerson> findAllBetween(int begin, int end) throws ServiceException;
    Map<Integer, String> finaAllNames() throws ServiceException;
    int countMediaPersons() throws ServiceException;
    Map.Entry<Optional<MediaPerson>, Optional<String>> findById(String stringMediaPersonId) throws ServiceException;
    Map.Entry<Optional<MediaPerson>, Optional<String>> update(String stringId, String firstName, String secondName,
                                                              String bio, String stringOccupationId, String stringBirthday,
                                                              String picture, String[] stringMoviesId) throws ServiceException;
    Map.Entry<Optional<MediaPerson>, Optional<String>> create(String firstName, String secondName,
                                                              String bio, String stringOccupationId, String stringBirthday,
                                                              String picture, String[] stringMoviesId) throws ServiceException;
    Optional<String> delete(String stringId) throws ServiceException;
    Map.Entry<List<String>, List<String>> validateData(String firstName, String secondName, String bio, String stringOccupationId,
                                                       String stringBirthday, String picture, String[] stringMoviesId);
}
