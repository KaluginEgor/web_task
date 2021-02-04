package com.example.demo_web.connection;

import com.example.demo_web.exception.ConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class ConnectionFactory {
    private static Logger logger = LogManager.getLogger(ConnectionFactory.class.getName());
    private static final String CONNECTION_ERROR = "Failed to create a database connection";
    private static final String READ_ERROR ="Could not read the database property file ";
    private static final String PROPERTIES_PATH = "/database.properties";
    private static final String URL = "db.url";
    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";
    private static final Properties properties = new Properties();

    ConnectionFactory() {
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH)) {
            properties.load(input);
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (IOException e) {
            logger.error("Could not read the property file!");
            //throw new ConnectionPoolException(READ_ERROR, e);
        } catch (SQLException e) {
            logger.error("Could not register driver: " + e);
            //throw new ConnectionPoolException(READ_ERROR, e);
        }
    }

    Connection create() throws ConnectionException {
        Connection connection;
        try {
            String databaseUrl = properties.getProperty(URL);
            String databaseUsername = properties.getProperty(USERNAME);
            String databasePassword = properties.getProperty(PASSWORD);
            connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
        } catch (SQLException e) {
            logger.error("Failed to create a database connection!");
            throw new ConnectionException(CONNECTION_ERROR, e);
        }
        logger.debug("Connection created.");
        return connection;
    }
}
