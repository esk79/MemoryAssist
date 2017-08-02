package utils;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by EvanKing on 7/31/17.
 */
public class CryptographyTest {

    private static final Random RANDOM = new SecureRandom();


    @Test
    public void testHashEqual() throws Exception {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        byte[] hash1 = Cryptography.hash(salt, "test_password");
        byte[] hash2 = Cryptography.hash(salt, "test_password");
        assertArrayEquals(hash1, hash2);

    }

    @Test
    public void testHashNotEqual() throws Exception {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        byte[] hash1 = Cryptography.hash(salt, "test_password");
        byte[] hash2 = Cryptography.hash(salt, "test_password_different");
        assertFalse(Arrays.equals(hash1, hash2));
    }
}