package com.epam.project.model.pool;

import com.epam.project.exception.ConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The type Connection factory.
 */
class ConnectionFactory {
    private static Logger logger = LogManager.getLogger(ConnectionFactory.class.getName());
    private static final String PROPERTIES_PATH = "/property/database.properties";
    private static final String URL = "url";
    private static final Properties properties = new Properties();
    private final String urlValue;

    /**
     * Instantiates a new Connection factory.
     */
    ConnectionFactory() {
        try (InputStream input = ConnectionFactory.class.getResourceAsStream(PROPERTIES_PATH)) {
            properties.load(input);
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            urlValue = (String) properties.get(URL);
        } catch (IOException | SQLException e) {
            logger.fatal(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Create connection connection.
     *
     * @return the connection
     * @throws ConnectionException the connection exception
     */
    Connection createConnection() throws ConnectionException {
        Connection connection;
        try {
            connection = DriverManager.getConnection(urlValue, properties);
        } catch (SQLException e) {
            logger.error(e);
            throw new ConnectionException(e);
        }
        return connection;
    }
}
