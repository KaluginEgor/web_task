package com.example.demo_web.model.service;

import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.OccupationType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MediaPersonService {
    List<MediaPerson> findAllBetween(int begin, int end) throws ServiceException;
    List<MediaPerson> finaAll() throws ServiceException;
    int countMediaPersons() throws ServiceException;
    Map.Entry<Optional<MediaPerson>, Optional<String>> findById(String stringMediaPersonId) throws ServiceException;
    MediaPerson update(int id, String firstName, String secondName, String bio, OccupationType occupationType, LocalDate birthday, String picture, String[] moviesId) throws ServiceException;
    MediaPerson create(String firstName, String secondName, String bio, OccupationType occupationType, LocalDate birthday, String picture, String[] moviesId) throws ServiceException;
    boolean delete(int id) throws ServiceException;
}
