package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class Statements {

    // Disallow instances of this class
    private Statements() {
    }

    // Update statements
    public static final String DB_NAME = "thalamus";
    public static final String CREATE_AUTHENTICATION_TABLE = "CREATE TABLE IF NOT EXISTS authenticator ("
            + "salt varbinary(256) NOT NULL,"
            + "hash varbinary(2048) NOT NULL"
            + ")";
    public static final String CREATE_RESOURCE_TABLE = "CREATE TABLE IF NOT EXISTS resource ("
            + "uid varchar(64) NOT NULL,"
            + "title varchar(512) NOT NULL,"
            + "markdown text NOT NULL,"
            + "PRIMARY KEY (uid),"
            + "UNIQUE (uid)"
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
        return provider("INSERT INTO authenticator (salt, hash) VALUES (?, ?)",
                statement -> {
                    statement.setBytes(1, salt);
                    statement.setBytes(2, hashedPassword);
                }
        );
    }

    //TODO: refactor these
    public static PreparedStatementProvider insertResource(String uid, String title, String markdown) throws SQLException {
        return provider("INSERT INTO resource (uid, title, markdown) VALUES (?, ?, ?)",
                statement -> {
                    statement.setString(1, uid);
                    statement.setString(2, title);
                    statement.setString(3, markdown);
                }
        );
    }

    public static PreparedStatementProvider updateResource(String uid, String title, String markdown) throws SQLException {
        return provider("UPDATE resource SET title = ?, markdown = ? WHERE uid = ?",
                statement -> {
                    statement.setString(1, title);
                    statement.setString(2, markdown);
                    statement.setString(3, uid);
                }
        );
    }

    public static PreparedStatementProvider deleteResource(String uid) throws SQLException {
        return provider("DELETE FROM resource WHERE uid = ?",
                statement -> {
                    statement.setString(1, uid);
                }
        );
    }

    public static PreparedStatementProvider getResource(String uid)
            throws SQLException {
        return provider("SELECT * FROM resource WHERE uid = ?",
                statement -> statement.setString(1, uid)
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
