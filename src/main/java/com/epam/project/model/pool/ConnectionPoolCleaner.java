package com.epam.project.model.pool;

class ConnectionPoolCleaner implements Runnable {
    @Override
    public void run() {
        ConnectionPool.getInstance().removeUnnecessaryConnections();
    }
}