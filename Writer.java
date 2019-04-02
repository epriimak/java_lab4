import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * class that write result of thread's work
 */
public class Writer {
    private FileOutputStream fOut;
    private final static String REGEX = " :: ";
    private final static String REGEX_NEW_LINE = "\n";
    private Map<Integer, String> result = new TreeMap<>();
    /**
     * init output file
     *
     * @param fOutName name
     * @throws FileNotFoundException if file not found
     */
    Writer(String fOutName) throws FileNotFoundException {
        fOut = new FileOutputStream(fOutName);
    }

    /**
     * write bytes in file(threads can't write at the same time)
     *
     * @param bytes bytes
     * @param pos   position of fist byte in input file
     * @throws IOException if file is not exist
     */
    public synchronized void putResult(byte[] bytes, int pos, int size) throws IOException {
        result.put(new Integer(pos), REGEX + size + REGEX + new String(bytes));
    }

    public void writeResults() throws IOException {
        for(Integer i:  result.keySet()){
            fOut.write(String.valueOf(i.intValue()).getBytes());
            fOut.write(result.get(i).getBytes());
            fOut.write(REGEX_NEW_LINE.getBytes());
        }
    }
}
