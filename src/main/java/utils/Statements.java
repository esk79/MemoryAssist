package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class Statements {

    // Disallow instances of this class
    private Statements() {
    }

    // Update statements
    public static final String DB_NAME = "memoryassist";
    public static final String CREATE_AUTHENTICATION_TABLE = "CREATE TABLE IF NOT EXISTS authenticator ("
            + "salt varbinary(32) NOT NULL,"
            + "hash varbinary(2048) NOT NULL"
            + ")";
    public static final String CREATE_RESOURCE_TABLE = "CREATE TABLE IF NOT EXISTS resource ("
            + "id int NOT NULL AUTO_INCREMENT,"
            + "title varchar(512) NOT NULL,"
            + "markdown text NOT NULL,"
            + "PRIMARY KEY (id),"
            + "UNIQUE (title)"
            + ")";

    public static final String GET_AUTHENTICATOR = "SELECT * FROM authenticator";

    private static PreparedStatementProvider provider(String query,
                                                      Populator populator) throws SQLException {
        return conn -> {
            PreparedStatement statement = conn.prepareStatement(query);
            try {
                populator.populate(statement);
                return statement;
            } catch (SQLException e) {
                statement.close();
                throw e;
            }
        };
    }


    public static PreparedStatementProvider insertAuthentication(byte[] salt, byte[] hashedPassword) throws SQLException {
        return provider("INSERT INTO authenticator (title, markdown) VALUES (?, ?)",
                statement -> {
                    statement.setBytes(1, salt);
                    statement.setBytes(2, hashedPassword);
                }
        );
    }

    //TODO: refactor these
    public static PreparedStatementProvider insertResource(String title, String markdown) throws SQLException {
        return provider("INSERT INTO resource (title, markdown) VALUES (?, ?)",
                statement -> {
                    statement.setString(1, title);
                    statement.setString(2, markdown);
                }
        );
    }

    public static PreparedStatementProvider getResource(int resourceID)
            throws SQLException {
        return provider("SELECT * FROM resource WHERE id = ?",
                statement -> statement.setInt(1, resourceID)
        );
    }

    @FunctionalInterface
    public interface PreparedStatementProvider {
        PreparedStatement get(Connection conn) throws SQLException;
    }

    @FunctionalInterface
    public interface Populator {
        void populate(PreparedStatement statement) throws SQLException;
    }
}
