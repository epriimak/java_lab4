import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * parsing config file of algorithm
 */
public class ParseCodingConfig {
    public enum mode {C, D}

    private enum config {MODE, NUM, TABLE}

    private mode realMode;
    private String fTableName;
    private int numBytes;

    private static Map<config, String> configKeys = new HashMap<>();
    private Map<String, String> configArgs = new HashMap<>();

    private final static int SIZE = 3;
    private final static String REGEX = ":";

    /**
     * initialization of keys
     */
    static {
        configKeys.put(config.MODE, "mode");
        configKeys.put(config.NUM, "num");
        configKeys.put(config.TABLE, "table");
    }

    /**
     * set mode using enum
     *
     * @param strMode stringMode
     * @throws ParseCodingConfigException if config file is not correct
     */
    private void SetMode(String strMode) throws ParseCodingConfigException {
        if (strMode.equals("code"))
            realMode = mode.C;
        else if (strMode.equals("decode"))
            realMode = mode.D;
        else
            throw new ParseCodingConfigException("Invalidate mode");
    }

    /**
     * parsing configuration file
     *
     * @param fName file name
     * @throws IOException                if config file is not exist
     * @throws ParseCodingConfigException if config file is not correct
     */
    ParseCodingConfig(String fName) throws IOException, ParseCodingConfigException {
        BufferedReader reader = new BufferedReader(new FileReader(fName));
        String str;

        while ((str = reader.readLine()) != null) {
            String[] parts = str.split(REGEX);
            configArgs.put(parts[0].replaceAll("\\s*", ""), parts[1].replaceAll("\\s*", ""));

            if (configKeys.containsValue(parts[0].replaceAll("\\s*", "")) == false) {
                throw new ParseCodingConfigException("Incorrect config file");
            }
        }

        if (configArgs.size() != SIZE) {
            throw new ParseCodingConfigException("Incorrect config file");
        }

        fTableName = configArgs.get(configKeys.get(config.TABLE));
        if (!fTableName.contains("txt")) {
            throw new ParseCodingConfigException("Incorrect table name");
        }

        numBytes = Integer.parseInt(configArgs.get(configKeys.get(config.NUM)));
        if (numBytes <= 0) {
            throw new ParseCodingConfigException("Wrong number of code bytes");
        }

        SetMode(configArgs.get(configKeys.get(config.MODE)));
    }

    /**
     * get mode
     *
     * @return mode
     */
    public mode GetMode() {
        return realMode;
    }

    /**
     * get number of bytes for encoding
     *
     * @return number of bytes
     */
    public int NumBytes() {
        return numBytes;
    }

    /**
     * get name of byte table
     *
     * @return file name
     */
    public String GetFTableName() {
        return fTableName;
    }
}

class ParseCodingConfigException extends Exception {
    ParseCodingConfigException(String message) {
        super(message);
    }
}

