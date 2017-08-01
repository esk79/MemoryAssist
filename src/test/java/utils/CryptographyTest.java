package utils;

import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by EvanKing on 7/31/17.
 */
public class CryptographyTest {

    private final SecureRandom random = new SecureRandom();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testHashEqual() throws Exception {
        byte[] salt = ByteUtil.hexStringToByteArray("test_salt");

        String hash1 = Cryptography.hash(salt, "test_password");
        String hash2 = Cryptography.hash(salt, "test_password");
        assertEquals(hash1, hash2);

    }

    @Test
    public void testHashNotEqual() throws Exception {
        byte[] salt = ByteUtil.hexStringToByteArray("test_salt");

        String hash1 = Cryptography.hash(salt, "test_password");
        String hash2 = Cryptography.hash(salt, "test_password_different");
        assertNotEquals(hash1, hash2);

    }
}