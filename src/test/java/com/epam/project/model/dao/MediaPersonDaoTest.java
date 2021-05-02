package com.epam.project.model.dao;

import com.epam.project.exception.ConnectionException;
import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.MediaPerson;
import com.epam.project.model.entity.OccupationType;
import com.epam.project.model.pool.ConnectionPool;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class MediaPersonDaoTest {
    private AbstractMediaPersonDao mediaPersonDao;
    private EntityTransaction transaction;
    private MediaPerson mediaPerson;

    @BeforeClass
    public void initialize() {
        mediaPersonDao = MediaPersonDao.getInstance();
        transaction = new EntityTransaction();
        transaction.init(mediaPersonDao);
        mediaPerson = new MediaPerson();
        mediaPerson.setId(0);
        mediaPerson.setFirstName("name");
        mediaPerson.setSecondName("name");
        mediaPerson.setBio("bio");
        mediaPerson.setBirthday(LocalDate.now());
        mediaPerson.setPicture("picture.png");
        mediaPerson.setOccupationType(OccupationType.ACTOR);
    }

    @BeforeMethod
    public void initializeTest() throws DaoException {
        mediaPerson = mediaPersonDao.create(mediaPerson);
    }

    @Test
    public void testIdExists() throws DaoException {
        boolean state = mediaPersonDao.idExists(mediaPerson.getId());
        assertTrue(state);
    }

    @Test
    public void testFindEntityById() throws DaoException {
        MediaPerson result = mediaPersonDao.findEntityById(mediaPerson.getId());
        assertEquals(mediaPerson, result);
    }

    @Test
    public void testIsUnique() throws DaoException {
        boolean state = mediaPersonDao.isUnique(mediaPerson.getFirstName(), mediaPerson.getSecondName(), mediaPerson.getBirthday());
        assertFalse(state);
    }

    @AfterMethod
    public void endTest() throws DaoException {
        mediaPersonDao.delete(mediaPerson.getId());
    }

    @AfterClass
    public void clear() {
        transaction.end();
        transaction = null;
        mediaPerson = null;
        mediaPersonDao = null;
        try {
            ConnectionPool.getInstance().destroyPool();
        } catch (ConnectionException e) {
            Assert.fail(e.getMessage());
        }
    }
}