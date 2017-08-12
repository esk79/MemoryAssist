package searchengine;


import annotations.BackupIndexDirectoryString;
import annotations.IndexDirectoryString;
import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import utils.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by EvanKing on 8/10/17.
 */
public class IndexBackupThread implements Runnable {
    private static final Log LOGGER = Log.forClass(IndexBackupThread.class);
    private final static long ONE_DAY_IN_MILLISECOND = 86400000;

    private final File indexDirectory;
    private final String backupIndexDirectoryString;

    @Inject
    public IndexBackupThread(@IndexDirectoryString String indexDirectoryString, @BackupIndexDirectoryString String backupIndexDirectoryString) throws IOException {
        indexDirectory = new File(indexDirectoryString);
        this.backupIndexDirectoryString = backupIndexDirectoryString;
    }


    @Override
    public void run() {
        ScheduledExecutorService execService = Executors.newScheduledThreadPool(5);
        execService.scheduleAtFixedRate(()-> backupIndex(),ONE_DAY_IN_MILLISECOND, ONE_DAY_IN_MILLISECOND, TimeUnit.MILLISECONDS);
    }

    private void backupIndex() {
        File backupLocation = getBackupDirectory();
        try {
            FileUtils.copyDirectory(indexDirectory, backupLocation);
        } catch (IOException e) {
            LOGGER.severe("[-] Unable to create index backup: %s", e.getMessage());
        }
    }

    private File getBackupDirectory() {
        Random random = new Random();
        String timeStamp = new SimpleDateFormat("HH-MM.dd.yyyy").format(new Date());
        File backupIndexDirectoryPath = new File(backupIndexDirectoryString + "/" + timeStamp);
        return backupIndexDirectoryPath;
    }


}
