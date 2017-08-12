package dao;

import com.google.inject.Inject;
import models.Authenticator;
import utils.ConnectionProvider;
import utils.Log;
import utils.Statements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by EvanKing on 7/31/17.
 */
public class AuthenticatorAccess extends AbstractAccess {

    private static final Log LOGGER = Log.forClass(AuthenticatorAccess.class);

    @Inject
    public AuthenticatorAccess(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public Optional<Authenticator> getAuthenticator() throws SQLException {
        return select(conn -> conn.prepareStatement(Statements.GET_AUTHENTICATOR), optional(this::getAuthenticator));
    }

    public void insertAuthentication(byte[] salt, byte[] hashedPassword) throws SQLException {

        if (getAuthenticator().isPresent()) {
            LOGGER.severe("Authentication table already contains entry. Please remove in order to update password.");
            return;
        }

        update(Statements.insertAuthentication(salt, hashedPassword), 1);
    }

    private Authenticator getAuthenticator(ResultSet resultSet) throws SQLException {
        byte[] salt = resultSet.getBytes("salt");
        byte[] hash = resultSet.getBytes("hash");

        return new Authenticator(salt, hash);
    }

}
