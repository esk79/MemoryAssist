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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by EvanKing on 8/10/17.
 */
public class IndexBackupThread implements Runnable {
    private static final Log LOGGER = Log.forClass(IndexBackupThread.class);
    private static final int MAX_NUMBER_OF_BACKUPS = 14;


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
        execService.scheduleAtFixedRate(() -> backupIndex(), 1, 1, TimeUnit.DAYS);
    }

    private void backupIndex() {
        File backupLocation = getBackupDirectory();
        try {
            deleteOldestBackup();
            FileUtils.copyDirectory(indexDirectory, backupLocation);
        } catch (IOException e) {
            LOGGER.severe("[-] Unable to create index backup: %s", e.getMessage());
        }
    }

    private File getBackupDirectory() {
        String timeStamp = new SimpleDateFormat("HH-MM.dd.yyyy").format(new Date());
        File backupIndexDirectoryPath = new File(backupIndexDirectoryString + "/" + timeStamp);
        return backupIndexDirectoryPath;
    }

    private void deleteOldestBackup() throws IOException {
        File backupLocation = new File(backupIndexDirectoryString);
        if (numberOfBackups(backupLocation) > MAX_NUMBER_OF_BACKUPS) {
            FileUtils.deleteDirectory(getOldestBackupDirectory(backupLocation));
        }
    }

    private int numberOfBackups(File backupLocation) {
        return backupLocation.listFiles().length;
    }

    private File getOldestBackupDirectory(File backupLocation) {
        long oldestTime = Long.MAX_VALUE;
        File oldestBackup = null;
        for (File backup : backupLocation.listFiles()) {
            if (backup.lastModified() < oldestTime) {
                oldestTime = backup.lastModified();
                oldestBackup = backup;
            }
        }
        return oldestBackup;
    }


}
