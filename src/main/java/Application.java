import com.google.inject.Guice;
import com.google.inject.Injector;
import controllers.IndexController;
import utils.IOUtils;

import java.io.IOException;
import java.util.Properties;

import static spark.Spark.port;
import static spark.Spark.staticFiles;

/**
 * Created by EvanKing on 7/19/17.
 */
public class Application {
    public static boolean run(String propertiesFilePath) {

        Properties serverProp = IOUtils.getPropertyFileObject(propertiesFilePath);
        int serverPort;
        try {
            serverPort = Integer.parseInt(IOUtils.getPropertyFromPropertiesFile(serverProp, "serverPort"));
        } catch (IOException | NumberFormatException e) {
            System.err.printf(String.format("Error: %s", e.getMessage()));
            return false;
        }
        // Configure Spark on port `port`
        port(serverPort);
        // Static files location
        staticFiles.location("/public");
        // Caching of static files lifetime
        staticFiles.expireTime(600L);

        Injector injector = Guice.createInjector();
        injector.getInstance(IndexController.class).init();

        return true;
    }
}
