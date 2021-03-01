package com.example.demo_web.service;

import com.example.demo_web.entity.Actor;
import com.example.demo_web.entity.Movie;
import com.example.demo_web.exception.ServiceException;

import java.util.List;

public interface ActorService {
    List<Actor> findAll(int begin, int end) throws ServiceException;
    int countActors() throws ServiceException;
}
