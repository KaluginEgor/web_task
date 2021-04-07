package com.example.demo_web.model.pool;

class ConnectionPoolCleaner implements Runnable {
    @Override
    public void run() {
        ConnectionPool.getInstance().removeUnnecessaryConnections();
    }
}