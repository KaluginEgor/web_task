package com.epam.project.model.pool;

import com.epam.project.exception.ConnectionException;
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
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    private ConnectionPool() {
        availableConnections = new LinkedBlockingDeque<>(MAX_POOL_SIZE);
        givenAwayConnections = new ArrayDeque<>(MAX_POOL_SIZE);
        initializePool();
    }

    private void initializePool() {
        ProxyConnection proxyConnection;
        ConnectionPoolCleaner cleaner = new ConnectionPoolCleaner();
        for (int i = 0; i < MIN_POOL_SIZE; i++) {
            try {
                proxyConnection = new ProxyConnection(factory.createConnection());
                availableConnections.offer(proxyConnection);
            } catch (ConnectionException e) {
                logger.fatal(e);
                throw new RuntimeException(e);
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
        ProxyConnection proxyConnection = null;
        if (!availableConnections.isEmpty() || detectPoolSize().get() == MAX_POOL_SIZE) {
            try {
                proxyConnection = availableConnections.take();
                givenAwayConnections.offer(proxyConnection);
                logger.info("Connection given from available connections");
            } catch (InterruptedException e) {
                logger.error(e);
                Thread.currentThread().interrupt();
            }
        } else {
            proxyConnection = new ProxyConnection(factory.createConnection());
            givenAwayConnections.offer(proxyConnection);
            logger.info("Connection created and given");
        }
        givenPerPeriodConnections.incrementAndGet();
        return proxyConnection;
    }

    public void returnConnection(ProxyConnection connection) throws ConnectionException {
        if (connection instanceof ProxyConnection) {
            if (givenAwayConnections.contains(connection)) {
                availableConnections.offer(connection);
                givenAwayConnections.remove(connection);
            }
        } else {
            logger.error("Error: not proxy connection");
            throw new ConnectionException("Not proxy connection.");
        }
    }

    private AtomicInteger detectPoolSize() {
        return new AtomicInteger(availableConnections.size() + givenAwayConnections.size());
    }

    void removeUnnecessaryConnections() {
        int removedConnections = 0;
        if (givenPerPeriodConnections.get() < VALUE_TO_DECREASE_POOL_PER_PERIOD) {
            int connectionsToRemoveCount = DECREASE_POOL_SIZE_NUMBER;
            while (connectionsToRemoveCount != 0 && !availableConnections.isEmpty()) {
                try {
                    ProxyConnection proxyConnectionToClose = availableConnections.take();
                    proxyConnectionToClose.reallyClose();
                    removedConnections++;
                    connectionsToRemoveCount--;
                } catch (SQLException e) {
                    logger.error(e);
                } catch (InterruptedException e) {
                    logger.error(e);
                    Thread.currentThread().interrupt();
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
                logger.error(e);
            }
        });
    }

    public void destroyPool() throws ConnectionException {
        for (int i = 0; i < detectPoolSize().get(); i++) {
            try {
                availableConnections.take().reallyClose();
            } catch (SQLException e) {
                logger.error(e);
                throw new ConnectionException(e);
            } catch (InterruptedException e) {
                logger.error(e);
                Thread.currentThread().interrupt();
            }
        }
        deregisterDrivers();
        logger.info("Connection pool destroyed");
    }
}
