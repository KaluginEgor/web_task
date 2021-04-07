package com.example.demo_web.model.pool;

import com.example.demo_web.exception.ConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class.getName());
    private static final String THREAD_ERROR = "Thread operation error: cause ";

    private static final int MAX_POOL_SIZE = 8;
    private static final int MIN_POOL_SIZE = 2;
    private AtomicInteger givenPerPeriodConnections = new AtomicInteger(0);
    private static final int VALUE_TO_DECREASE_POOL_PER_PERIOD = 100;
    private static final int DECREASE_POOL_SIZE_NUMBER = 2;

    private static final int CLEAR_PERIOD_IN_MINUTES = 60;

    private final ConnectionFactory factory = new ConnectionFactory();
    private static ConnectionPool instance;
    private BlockingQueue<ProxyConnection> availableConnections;
    private Queue<ProxyConnection> givenAwayConnections;
    public static final AtomicBoolean isNull = new AtomicBoolean(true);
    private static final Lock LOCK = new ReentrantLock();
    private final Lock connectionLock = new ReentrantLock();
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    private ConnectionPool() {
        availableConnections = new LinkedBlockingDeque<>(MAX_POOL_SIZE);
        givenAwayConnections = new ArrayDeque<>(MAX_POOL_SIZE);
        initializePool();
    }

    private void initializePool() {
        Connection connection;
        ConnectionPoolCleaner cleaner = new ConnectionPoolCleaner();
        for (int i = 0; i < MIN_POOL_SIZE; i++) {
            try {
                connection = factory.createConnection();
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                availableConnections.add(proxyConnection);
            } catch (ConnectionException e) {
                logger.error(e);
            }
        }
        executorService.scheduleAtFixedRate(cleaner, 0, CLEAR_PERIOD_IN_MINUTES, TimeUnit.MINUTES);
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
        logger.info("getting connection started");
        if (!availableConnections.isEmpty() || detectPoolSize().get() == MAX_POOL_SIZE) { //todo: mb synchronized?
            try {
                connectionLock.lock();
                connection = availableConnections.take();
                givenAwayConnections.offer(connection);
            } catch (InterruptedException e) {
                logger.error("Get connection error");
                throw new ConnectionException(THREAD_ERROR, e);
            } finally {
                connectionLock.unlock();
                logger.info("Connection given from available connections");
            }
        } else {
            try {
                connectionLock.lock();
                connection = new ProxyConnection(factory.createConnection());
                givenAwayConnections.offer(connection);
            } finally {
                connectionLock.unlock();
                logger.info("Connection created and given");
            }
        }
        givenPerPeriodConnections.incrementAndGet();
        logger.info("getting connection ended");
        return connection;
    }

    public void returnConnection(ProxyConnection connection) throws ConnectionException {
        logger.info("returning connection started");
        try {
            connectionLock.lock();
            if (connection instanceof ProxyConnection) {
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
        }
        logger.info("returning connection ended");
    }

    private AtomicInteger detectPoolSize() {
        return new AtomicInteger(availableConnections.size() + givenAwayConnections.size());
    }

    void removeUnnecessaryConnections() {
        int removedConnections = 0;
        if (givenPerPeriodConnections.get() < VALUE_TO_DECREASE_POOL_PER_PERIOD) {
            int connectionsToRemoveCount = DECREASE_POOL_SIZE_NUMBER;
            while (connectionsToRemoveCount-- != 0 && !availableConnections.isEmpty()) {
                try {
                    ProxyConnection proxyConnectionToClose = availableConnections.take();
                    proxyConnectionToClose.reallyClose();
                    removedConnections++;
                } catch (InterruptedException | SQLException exception) {
                    logger.error(exception.getMessage());
                }
            }
        }
        givenPerPeriodConnections.set(0);
        logger.info("Removed {} connections", removedConnections);
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
        for (int i = 0; i < detectPoolSize().get(); i++) {
            try {
                availableConnections.take().reallyClose();
            } catch (SQLException e) {
                logger.error(e);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        deregisterDrivers();
        logger.info("Connection pool destroyed");
    }
}
