package com.example.demo_web.connection;

import com.example.demo_web.exception.ConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class.getName());
    private static final String THREAD_ERROR = "Thread operation error: cause ";
    private static final int POOL_SIZE = 10;

    private static ConnectionPool instance;
    //private final Semaphore semaphore = new Semaphore(POOL_SIZE);
    private BlockingQueue<ProxyConnection> availableConnections;
    private Queue<ProxyConnection> givenAwayConnections;
    public static AtomicBoolean isNull = new AtomicBoolean(true);
    private static final Lock LOCK = new ReentrantLock();
    private final Lock connectionLock = new ReentrantLock();

    private ConnectionPool() {
        availableConnections = new LinkedBlockingDeque<>(POOL_SIZE);
        givenAwayConnections = new ArrayDeque<>();
        initializePool();
    }

    private void initializePool() {
        ConnectionFactory factory = new ConnectionFactory();
        for (int i = 0; i < POOL_SIZE; i++) {
            Connection connection;
            try {
                connection = factory.create();
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                availableConnections.add(proxyConnection);
            } catch (ConnectionException e) {
                logger.error(e); //todo
            }
        }
    }

    public static ConnectionPool getInstance() {
        if (isNull.get()) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new ConnectionPool();
                    isNull.set(false);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection() throws ConnectionException {
        ProxyConnection connection;
        try {
            //semaphore.acquire();
            connectionLock.lock();
            connection = availableConnections.take();
            givenAwayConnections.offer(connection);
        } catch (InterruptedException e) {
            logger.error("Get connection error");
            throw new ConnectionException(THREAD_ERROR, e);
        } finally {
            connectionLock.unlock();
        }
        return connection;
    }

    public void returnConnection(ProxyConnection connection) throws ConnectionException {
        try {
            connectionLock.lock();
            if (connection instanceof ProxyConnection) {
                connectionLock.lock();
                if (givenAwayConnections.contains(connection)) {
                    availableConnections.offer(connection);
                    givenAwayConnections.remove(connection);
                }
            } else {
                logger.error("error: not proxy connection");
                throw new ConnectionException("Not proxy connection.");
            }
        } finally {
            connectionLock.unlock();
            //semaphore.release();
        }
    }

    private void deregisterDrivers() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("Failed to deregister driver", e);
            }
        });
    }

    public void destroyPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                availableConnections.take().close();
            } catch (SQLException e) {
                logger.error(e);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        deregisterDrivers();
    }
}
