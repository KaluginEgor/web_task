package com.example.demo_web.dao.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.builder.impl.MediaPersonBuilder;
import com.example.demo_web.connection.ConnectionPool;
import com.example.demo_web.dao.MediaPersonDao;
import com.example.demo_web.entity.MediaPerson;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MediaPersonDaoImpl implements MediaPersonDao {
    private static MediaPersonDao instance = new MediaPersonDaoImpl();
    private static final BaseBuilder<MediaPerson> actorBuilder = new MediaPersonBuilder();

    private static final String SQL_SELECT_ALL_MEDIA_PERSONS_WITH_LIMIT = "SELECT MP.media_person_id, MP.media_person_first_name, MP.media_person_second_name, MPO.media_person_occupation_name, MP.media_person_bio, MP.media_person_birthday, MP.media_person_picture FROM media_persons MP INNER JOIN media_person_occupation MPO on MP.media_person_occupation_id = MPO.media_person_occupation_id WHERE MP.media_person_is_deleted = 0 LIMIT ?, ?;";

    private static final String SQL_COUNT_MEDIA_PERSONS = "SELECT COUNT(*) AS media_person_count FROM media_persons WHERE media_person_is_deleted = 0;";

    private static final String SQL_SELECT_MEDIA_PERSONS_BY_MOVIE_ID = "SELECT MP.media_person_id, MP.media_person_first_name, MP.media_person_second_name, MPO.media_person_occupation_name, MP.media_person_bio, MP.media_person_birthday, MP.media_person_picture FROM media_persons MP INNER JOIN media_person_occupation MPO on MP.media_person_occupation_id = MPO.media_person_occupation_id INNER JOIN media_persons_movies MPM on MP.media_person_id = MPM.media_person_id INNER JOIN movies M on MPM.movie_id = M.movie_id WHERE M.movie_id = ? AND MP.media_person_is_deleted = 0 AND M.movie_is_deleted = 0;";

    private static final String SQL_SELECT_MEDIA_PERSON_BY_ID = "SELECT MP.media_person_id, MP.media_person_first_name, MP.media_person_second_name, MPO.media_person_occupation_name, MP.media_person_bio, MP.media_person_birthday, MP.media_person_picture FROM media_persons MP INNER JOIN media_person_occupation MPO on MP.media_person_occupation_id = MPO.media_person_occupation_id WHERE MP.media_person_id = ? AND MP.media_person_is_deleted = 0;";

    private static final String SQL_UPDATE_MEDIA_PERSON = "UPDATE media_persons MP SET MP.media_person_first_name = ?, MP.media_person_second_name = ?, MP.media_person_occupation_id = ?, MP.media_person_bio = ?, MP.media_person_birthday = ?, MP.media_person_picture = ? WHERE MP.media_person_id = ?;";

    private MediaPersonDaoImpl(){}

    public static MediaPersonDao getInstance() {
        return instance;
    }

    @Override
    public List<MediaPerson> findAllBetween(int begin, int end) throws DaoException {
        List<MediaPerson> mediaPersonList = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_MEDIA_PERSONS_WITH_LIMIT)) {
            preparedStatement.setInt(1, begin);
            preparedStatement.setInt(2, end);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                MediaPerson mediaPerson;
                while (resultSet.next()) {
                    mediaPerson = actorBuilder.build(resultSet);
                    mediaPersonList.add(mediaPerson);
                }
            }
            return mediaPersonList;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int countMediaPersons() throws DaoException {
        int actorsCount = 0;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_MEDIA_PERSONS)) {
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
    public List<MediaPerson> findByMovieId(Integer id) throws DaoException {
        List<MediaPerson> crew = new ArrayList<>();
        MediaPerson mediaPerson;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MEDIA_PERSONS_BY_MOVIE_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                mediaPerson = actorBuilder.build(resultSet);
                crew.add(mediaPerson);
            }
            return crew;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<MediaPerson> findAll() throws DaoException {
        return null;
    }

    @Override
    public MediaPerson findEntityById(Integer id) throws DaoException {
        MediaPerson mediaPerson = null;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MEDIA_PERSON_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                mediaPerson = actorBuilder.build(resultSet);
            }
            return mediaPerson;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(MediaPerson mediaPerson) throws DaoException {
        return false;
    }

    @Override
    public boolean create(MediaPerson mediaPerson) throws DaoException {
        return false;
    }

    @Override
    public MediaPerson update(MediaPerson mediaPerson) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_MEDIA_PERSON)) {
            preparedStatement.setString(1, mediaPerson.getFirstName());
            preparedStatement.setString(2, mediaPerson.getSecondName());
            preparedStatement.setInt(3, mediaPerson.getOccupationType().ordinal());
            preparedStatement.setString(4, mediaPerson.getBio());
            preparedStatement.setDate(5, java.sql.Date.valueOf(mediaPerson.getBirthday()));
            preparedStatement.setString(6, mediaPerson.getPicture());
            preparedStatement.setInt(7, mediaPerson.getId());
            preparedStatement.executeUpdate();
            return mediaPerson;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }
}
