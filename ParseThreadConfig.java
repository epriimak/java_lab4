import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * parsinf config file of threads
 */
public class ParseThreadConfig {
    private enum config {BLOCK_SIZE, INC, START}

    private int sizeBlock;
    private int startPos;
    private int inc;

    private static Map<ParseThreadConfig.config, String> configKeys = new HashMap<>();
    private Map<String, String> configArgs = new HashMap<>();

    private int SIZE = 3;
    private String REGEX = ":";

    /**
     * initialization of keys
     */
    static {
        configKeys.put(ParseThreadConfig.config.BLOCK_SIZE, "size");
        configKeys.put(ParseThreadConfig.config.INC, "inc");
        configKeys.put(ParseThreadConfig.config.START, "startpos");
    }

    /**
     * parsing configuration file
     *
     * @param fName file name
     * @throws IOException                if config is not exist
     * @throws ParseCodingConfigException if config is not correct
     */
    ParseThreadConfig(String fName) throws IOException, ParseThreadConfigException {
        BufferedReader reader = new BufferedReader(new FileReader(fName));
        String str;

        while ((str = reader.readLine()) != null) {
            String[] parts = str.split(REGEX);
            configArgs.put(parts[0].replaceAll("\\s*", ""), parts[1].replaceAll("\\s*", ""));

            if (configKeys.containsValue(parts[0].replaceAll("\\s*", "")) == false) {
                throw new ParseThreadConfigException("Incorrect config file");
            }
        }

        if (configArgs.size() != SIZE) {
            throw new ParseThreadConfigException("Incorrect config file");
        }
        sizeBlock = Integer.parseInt(configArgs.get(configKeys.get(ParseThreadConfig.config.BLOCK_SIZE)));
        if (sizeBlock < 1) {
            throw new ParseThreadConfigException("Wrong number of code bytes");
        }

        inc = Integer.parseInt(configArgs.get(configKeys.get(ParseThreadConfig.config.INC)));
        if (inc < 1) {
            throw new ParseThreadConfigException("Wrong number of code bytes");
        }

        startPos = Integer.parseInt(configArgs.get(configKeys.get(ParseThreadConfig.config.START)));
        if (startPos < 0) {
            throw new ParseThreadConfigException("Wrong number of code bytes");
        }
    }

    /**
     * get block size
     *
     * @return block size
     */
    public int GetBlockSize() {
        return sizeBlock;
    }

    /**
     * get increment for thread
     *
     * @return increment
     */
    public int GetInc() {
        return inc;
    }

    /**
     * gettinf start position
     *
     * @return start position
     */
    public int GetStartPos() {
        return startPos;
    }
}


class ParseThreadConfigException extends Exception {
    ParseThreadConfigException(String message) {
        super(message);
    }
}
