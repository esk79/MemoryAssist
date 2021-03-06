import annotations.BackupIndexDirectoryString;
import annotations.DatabasePassword;
import annotations.DatabasePort;
import annotations.IndexDirectoryString;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import config.DatabaseConfig;
import controllers.AuthenticationController;
import controllers.HomeController;
import controllers.ResourceController;
import controllers.SearchController;
import searchengine.IndexBackupCronJob;
import spark.servlet.SparkApplication;
import utils.ConnectionProvider;
import utils.IOUtils;
import utils.ProductionConnectionProvider;

import java.io.IOException;
import java.util.Properties;

import static spark.Spark.port;
import static spark.Spark.secure;
import static spark.Spark.staticFiles;

/**
 * Created by EvanKing on 7/19/17.
 */
public class Application implements SparkApplication {

    @Override
    public void init() {
        run("src/main/resources/properties/server.properties");
    }

    private static boolean run(String propertiesFilePath) {
        if (!handleArgs(propertiesFilePath)) {
            return false;
        }

        Injector injector = Guice.createInjector(new Module());
        injector.getInstance(HomeController.class).init();
        injector.getInstance(AuthenticationController.class).init();
        injector.getInstance(ResourceController.class).init();
        injector.getInstance(SearchController.class).init();

        injector.getInstance(DatabaseConfig.class).dbInit();

        injector.getInstance(IndexBackupCronJob.class).startIndexBackupCronJob();

        return true;
    }

    private static boolean handleArgs(String propertiesFilePath){
        Properties serverProp = IOUtils.getPropertyFileObject(propertiesFilePath);

        int serverPort;
        String keystorePath;
        String keystorePassword;
        try {
            keystorePath = IOUtils.getPropertyFromPropertiesFile(serverProp, "keystore");
            keystorePassword = IOUtils.getPropertyFromPropertiesFile(serverProp, "keystorePassword");
            serverPort = Integer.parseInt(IOUtils.getPropertyFromPropertiesFile(serverProp, "serverPort"));

            Module.DB_PASSWORD = IOUtils.getPropertyFromPropertiesFile(serverProp, "databasePassword");
            Module.DB_PORT = IOUtils.getPropertyFromPropertiesFile(serverProp, "databasePort");
            Module.INDEX_DIRECTORY_STRING = IOUtils.getPropertyFromPropertiesFile(serverProp, "indexDirectoryPath");
            Module.BACKUP_INDEX_DIRECTORY_STRING = IOUtils.getPropertyFromPropertiesFile(serverProp, "backupIndexDirectoryPath");
        } catch (IOException | NumberFormatException e) {
            System.err.printf(String.format("Error: %s", e.getMessage()));
            return false;
        }

        configureSpark(serverPort);
        secure(keystorePath, keystorePassword, null, null);

        return true;
    }

    private static void configureSpark(int serverPort){
        // Configure Spark on port `port`
        port(serverPort);
        // Static files location
        staticFiles.location("/public");
        // Caching of static files lifetime
        staticFiles.expireTime(600L);
    }

    private static class Module extends AbstractModule {
        private static String DB_PASSWORD = null;
        private static String DB_PORT = null;
        private static String INDEX_DIRECTORY_STRING = null;
        private static String BACKUP_INDEX_DIRECTORY_STRING = null;

        @Override
        protected void configure() {
            bind(ConnectionProvider.class).to(ProductionConnectionProvider.class);

            //TODO: Refactor below
            if (DB_PASSWORD == null) {
                throw new IllegalStateException("DB_PASSWORD not initialized");
            }

            if (DB_PORT == null) {
                throw new IllegalStateException("DB_PASSWORD not initialized");
            }

            if (INDEX_DIRECTORY_STRING == null) {
                throw new IllegalStateException("INDEX_DIRECTORY_STRING not initialized");
            }

            if (BACKUP_INDEX_DIRECTORY_STRING == null) {
                throw new IllegalStateException("BACKUP_INDEX_DIRECTORY_STRING not initialized");
            }

            bind(String.class).annotatedWith(DatabasePassword.class).toInstance(DB_PASSWORD);
            bind(String.class).annotatedWith(DatabasePort.class).toInstance(DB_PORT);
            bind(String.class).annotatedWith(IndexDirectoryString.class).toInstance(INDEX_DIRECTORY_STRING);
            bind(String.class).annotatedWith(BackupIndexDirectoryString.class).toInstance(BACKUP_INDEX_DIRECTORY_STRING);
        }
    }
}
