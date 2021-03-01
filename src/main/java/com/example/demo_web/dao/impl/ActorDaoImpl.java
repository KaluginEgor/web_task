package com.example.demo_web.dao.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.builder.impl.ActorBuilder;
import com.example.demo_web.connection.ConnectionPool;
import com.example.demo_web.dao.ActorDao;
import com.example.demo_web.entity.Actor;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActorDaoImpl implements ActorDao {
    private static ActorDao instance = new ActorDaoImpl();
    private static final BaseBuilder<Actor> actorBuilder = new ActorBuilder();

    private static final String SQL_SELECT_ALL_ACTORS_WITH_LIMIT = "SELECT A.actor_id, A.actor_first_name, A.actor_second_name, A.actor_bio, A.actor_birthday, A.actor_picture FROM actors A LIMIT ?, ?;";

    private static final String SQL_COUNT_ACTORS = "SELECT COUNT(*) AS actors_count FROM actors;";

    private ActorDaoImpl(){}

    public static ActorDao getInstance() {
        return instance;
    }

    @Override
    public List<Actor> findAllBetween(int begin, int end) throws DaoException {
        List<Actor> actorList = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_ACTORS_WITH_LIMIT)) {
            preparedStatement.setInt(1, begin);
            preparedStatement.setInt(2, end);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Actor actor;
                while (resultSet.next()) {
                    actor = actorBuilder.build(resultSet);
                    actorList.add(actor);
                }
            }
            return actorList;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int countActors() throws DaoException {
        int actorsCount = 0;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_ACTORS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                actorsCount = resultSet.getInt(1);
            }
        } catch (ConnectionException | SQLException e) {
            //logger.error(e);
            throw new DaoException(e);
        }
        return actorsCount;
    }

    @Override
    public List<Actor> findAll() throws DaoException {
        return null;
    }

    @Override
    public Actor findEntityById(Integer id) throws DaoException {
        return null;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Actor actor) throws DaoException {
        return false;
    }

    @Override
    public boolean create(Actor actor) throws DaoException {
        return false;
    }

    @Override
    public Actor update(Actor actor) throws DaoException {
        return null;
    }
}
