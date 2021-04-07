package com.example.demo_web.model.service;

import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.exception.ServiceException;

import java.util.List;

public interface MediaPersonService {
    List<MediaPerson> findAll(int begin, int end) throws ServiceException;
    int countMediaPersons() throws ServiceException;
    MediaPerson findById(Integer id) throws ServiceException;
    MediaPerson update(MediaPerson mediaPerson) throws ServiceException;
    MediaPerson create(MediaPerson mediaPerson, List<Integer> moviesId) throws ServiceException;
}
