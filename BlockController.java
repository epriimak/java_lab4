import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * class, that gets block of bytes from file and gives to threads
 */
public class BlockController {
    private static int blockSize;
    private static FileInputStream inFile;
    private static int iPos = 0, iPosPrev = 0;
    private static byte[] arrBytes;
    private int newRest = 0, restSize = 0;
    private int maxPos = 0;
    private Map<Integer, Integer> posSizeInfo = new HashMap<>();
    private boolean endFile = false;

    /**
     * init class
     *
     * @param bSize   size of one block
     * @param fInName input file name
     * @throws IOException if file not open
     */
    BlockController(int bSize, String fInName) throws IOException {
        blockSize = bSize;
        inFile = new FileInputStream(fInName);
    }

    /**
     * get bytes with exact size from file
     *
     * @param restSize
     * @throws IOException if file is not found
     */
    public synchronized boolean GetBytesFromFile(int restSize) throws IOException {
        byte[] newArrBytes;
        if (iPos == 0)
            newArrBytes = new byte[blockSize];
        else
            newArrBytes = new byte[blockSize + restSize];

        for (int j = 0; j < restSize; j++) {
            newArrBytes[j] = arrBytes[arrBytes.length - restSize + j];
        }

        int b;
        for (int i = 0; i < blockSize; i++) {
            b = inFile.read();
            if(b != -1) {
                newArrBytes[i + restSize] = (byte)b;
            }
            else {
                endFile = true;
                if(i != blockSize - 1)
                    return false;
            }
        }
        iPosPrev = iPos;
        iPos += blockSize;
        arrBytes = newArrBytes;
        return true;
    }

    /**
     * giving block of bytes to threads
     *
     * @return block of bytes
     * @throws IOException if bytes can't be getted from file
     */
    public synchronized byte[] GetBlockBytes(int numBytes, int startPos) throws IOException {
        byte[] block = new byte[numBytes];
        if ((startPos > iPos + blockSize
                || startPos + numBytes > iPos)
                && !AllWaiting(startPos, numBytes)) {
            return null;
        }

        if (AllWaiting(startPos, numBytes) && endFile == false) {
            restSize = iPos - startPos;
            if(!GetBytesFromFile(restSize))
                return null;

            posSizeInfo.clear();
            newRest = restSize;
            restSize = 0;
        }

        if(maxPos < startPos)
            maxPos = startPos;

        int posInBlock = startPos - iPosPrev + newRest;
        for (int i = 0; i < numBytes && posInBlock + i < arrBytes.length; i++) {
            block[i] = arrBytes[posInBlock + i];
        }
        posSizeInfo.put(new Integer(startPos), new Integer(numBytes));
        return block;
    }

    /**
     * answer, if all bytes from big block were used
     *
     * @param pos  position, that something try to get
     * @param size size
     * @return true, if all bytes were used
     */
    synchronized boolean AllWaiting(int pos, int size) {
        if (posSizeInfo.size() == 0)
            return false;

        int sizeAll = 0;
        int posInBlock = pos - iPosPrev + newRest;

        for (Integer i : posSizeInfo.values())
            sizeAll += i.intValue();

        if (pos + size > maxPos + posSizeInfo.get(maxPos)  &&
                posInBlock == sizeAll && pos + size > iPos )
            return true;

        return false;
    }

    /**
     * answer, if bytes, that someone ask, is not in our thread
     *
     * @param size   size
     * @param curPos position
     * @return true, if all bytes in thread were used
     */
    public boolean NotEnd(int size, int curPos) {
        if (curPos + size > iPos && endFile == true)
            return false;

        return true;
    }


}
