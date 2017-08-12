package searchengine;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Created by EvanKing on 8/10/17.
 */
public class IndexBackupCronJob {
    private final Provider<IndexBackupThread> backupThreadProvider;

    @Inject
    public IndexBackupCronJob(Provider<IndexBackupThread> backupThreadProvider) {
        this.backupThreadProvider = backupThreadProvider;
    }

    public void startIndexBackupCronJob() {
        new Thread(backupThreadProvider.get()).start();
    }

}
