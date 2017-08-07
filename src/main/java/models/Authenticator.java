package models;

import utils.Cryptography;
import utils.Log;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * Created by EvanKing on 7/31/17.
 */
public class Authenticator {
    private static final Log LOGGER = Log.forClass(Authenticator.class);

    private byte[] salt;
    private byte[] hash;

    public Authenticator(byte[] salt, byte[] hash) {
        this.salt = salt;
        this.hash = hash;
    }

    public boolean authenticate(String password) {
        byte[] userHash = null;

        try {
            userHash = Cryptography.hash(salt, password);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.severe("Error hashing inputted password: %s", e.getMessage());
        } catch (InvalidKeySpecException e) {
            LOGGER.severe("Error hashing inputted password: %s", e.getMessage());
        }

        return Arrays.equals(userHash, hash);
    }

}
