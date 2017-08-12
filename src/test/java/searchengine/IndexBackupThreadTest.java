package searchengine;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by EvanKing on 8/10/17.
 */
public class IndexBackupThreadTest {

    private IndexBackupThread indexBackupThread;

    @Before
    public void setUp() throws Exception {
        indexBackupThread = new IndexBackupThread("src/test/resources/index","src/main/resources/backups");
    }

    @Test
    public void testCreateIndexBackup() throws Exception {
    }
}