package dao;

import com.google.inject.Inject;
import utils.ConnectionProvider;
import utils.Cryptography;
import utils.Statements;

import java.sql.SQLException;

/**
 * Created by EvanKing on 7/31/17.
 */
public class AuthenticatorAccess extends AbstractAccess{


    @Inject
    public AuthenticatorAccess(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public String getPasswordHash() throws SQLException {
        return select(conn -> conn.prepareStatement(Statements.GET_PASSWORD_HASH),
                resultSet -> resultSet.getString("hash"));
    }

    public byte[] getPasswordSalt() throws SQLException {
        return select(conn -> conn.prepareStatement(Statements.GET_PASSWORD_SALT),
                resultSet -> resultSet.getBytes("salt"));
    }

    //TODO; refactor
    public boolean authenticate(String password) {
        String correctHash = null;
        byte[] salt;
        String userHash = null;

        try {
            correctHash = getPasswordHash();
            salt = getPasswordSalt();
            userHash = Cryptography.hash(salt, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userHash.equals(correctHash);
    }

}
