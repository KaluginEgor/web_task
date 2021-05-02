package com.epam.project.model.pool;

/**
 * The type Connection pool cleaner.
 */
class ConnectionPoolCleaner implements Runnable {
    @Override
    public void run() {
        ConnectionPool.getInstance().removeUnnecessaryConnections();
    }
}