import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * parsing main config file
 */
public class ParseMainConfig {
    private enum config {BLOCK_SIZE, NUM_THREADS, ALG_CONFIG}

    private String fAlgConfigName;
    private int numThread, sizeBlock;

    private static Map<config, String> configKeys = new HashMap<>();
    private Map<String, String> configArgs = new HashMap<>();

    private final static int SIZE = 3;
    private final static String REGEX = ":";

    /**
     * initialization of keys
     */
    static {
        configKeys.put(config.BLOCK_SIZE, "blocksize");
        configKeys.put(config.NUM_THREADS, "thread'snum");
        configKeys.put(config.ALG_CONFIG, "algconfig");
    }

    /**
     * parsing configuration file
     *
     * @param fName file name
     * @throws IOException                if file is not exist
     * @throws ParseCodingConfigException if file is not correct
     */
    ParseMainConfig(String fName) throws IOException, ParseMainConfigException {
        BufferedReader reader = new BufferedReader(new FileReader(fName));
        String str;

        while ((str = reader.readLine()) != null) {
            String[] parts = str.split(REGEX);
            configArgs.put(parts[0].replaceAll("\\s*", ""), parts[1].replaceAll("\\s*", ""));

            if (configKeys.containsValue(parts[0].replaceAll("\\s*", "")) == false) {
                throw new ParseMainConfigException("Incorrect config files");
            }
        }

        if (configArgs.size() != SIZE) {
            throw new ParseMainConfigException("Incorrect config file");
        }

        fAlgConfigName = configArgs.get(configKeys.get(config.ALG_CONFIG));
        if (!fAlgConfigName.contains("txt")) {
            throw new ParseMainConfigException("Incorrect config name");
        }

        numThread = Integer.parseInt(configArgs.get(configKeys.get(config.NUM_THREADS)));
        if (numThread < 1) {
            throw new ParseMainConfigException("Wrong number of num threads");
        }

        sizeBlock = Integer.parseInt(configArgs.get(configKeys.get(config.BLOCK_SIZE)));
        if (sizeBlock <= 0) {
            throw new ParseMainConfigException("Wrong number of bloc size");
        }
    }

    /**
     * get size of block
     *
     * @return size of block
     */
    public int GetBlockSize() {
        return sizeBlock;
    }

    /**
     * get number of threads
     *
     * @return number of threads
     */
    public int GetNumThreads() {
        return numThread;
    }

    /**
     * get config file of algorithm
     *
     * @return
     */
    public String GetFAlfConfigName() {
        return fAlgConfigName;
    }


}

class ParseMainConfigException extends Exception {
    ParseMainConfigException(String message) {
        super(message);
    }
}



