package config;

import com.google.inject.Inject;
import utils.ConnectionProvider;
import utils.Log;
import utils.Statements;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to config and initialize DB with necessary tables. So far only creates a users table.
 * NOTE: unable to run sql scripts because of findbugs and dependency restrictions. See line 31.
 *
 * @version 1.0, March 11 2017
 */
public final class DatabaseConfig {
    private static final Log LOGGER = Log.forClass(DatabaseConfig.class);

    private final ConnectionProvider connectionProvider;

    @Inject
    public DatabaseConfig(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void dbInit() {
        LOGGER.info("[!] Initializing database");
        try (Connection connection = connectionProvider.getConnection()) {
            setUp(connection);
        } catch (Exception e) {
            LOGGER.severe("[-] Error " + e.getMessage());
        }
    }

    public static void setUp(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
           LOGGER.info("[!] Creating tables if not exist");
            statement.executeUpdate(Statements.CREATE_AUTHENTICATION_TABLE);
            statement.executeUpdate(Statements.CREATE_RESOURCE_TABLE);
        }
    }
}