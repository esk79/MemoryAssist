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
            + "hash varbinary(2048) NOT NULL,"
            + ")";

    public static final String GET_PASSWORD_HASH = "SELECT hash FROM authenticator";
    public static final String GET_PASSWORD_SALT = "SELECT salt FROM authenticator";


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

    //TODO: Understand this
    @FunctionalInterface
    public interface PreparedStatementProvider {
        PreparedStatement get(Connection conn) throws SQLException;
    }

    @FunctionalInterface
    public interface Populator {
        void populate(PreparedStatement statement) throws SQLException;
    }
}
