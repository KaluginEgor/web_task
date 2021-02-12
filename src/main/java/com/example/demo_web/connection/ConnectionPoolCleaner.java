package com.example.demo_web.connection;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

class ConnectionPoolCleaner implements Runnable {
    @Override
    public void run() {
        ConnectionPool.getInstance().removeUnnecessaryConnections();
    }
}