package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by EvanKing on 7/19/17.
 */
public class IOUtils {

    public static String getPropertyFromPropertiesFile(Properties prop, String key) throws IOException {
        if (prop.containsKey(key)) {
            return prop.getProperty(key);
        } else {
            throw new IOException("Property \'" + key + "\' is missing.");
        }
    }

    // TODO: Refactor
    public static Properties getPropertyFileObject(String propertiesFilePath) {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream(propertiesFilePath);

            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
}
