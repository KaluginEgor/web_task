package com.example.demo_web.dao;

import com.example.demo_web.entity.Actor;
import com.example.demo_web.entity.Movie;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public interface ActorDao extends BaseDao<Integer, Actor> {
    List<Actor> findAllBetween(int begin, int end) throws DaoException;
    int countActors() throws DaoException;
}
