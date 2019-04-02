import java.io.IOException;

/**
 * class that code bytes
 */
public class Coding {
    private byte[] inBytes;
    private byte[] outBytes;
    private ParseCodingConfig confFile;
    private Table tabMap;

    /**
     * init
     *
     * @param fConfigName config file name
     * @throws IOException                if config file not exist
     * @throws ParseCodingConfigException if congig file not correct
     * @throws TableException             if table not correct
     */
    public Coding(String fConfigName) throws IOException, ParseCodingConfigException, TableException {
        confFile = new ParseCodingConfig(fConfigName);

        tabMap = new Table(confFile.GetFTableName(), confFile.GetMode(), confFile.NumBytes());
        tabMap.MakeTable();
    }

    /**
     * running encoding
     *
     * @param inBytes code's bytes
     */
    public void Run(byte[] inBytes) {
        this.inBytes = new byte[inBytes.length];
        this.inBytes = inBytes;
        outBytes = new byte[inBytes.length];

        int sizeBytesArr = inBytes.length / confFile.NumBytes();
        int sizeRestBytesArr = inBytes.length % confFile.NumBytes();
        int iByteCur = 0, iByte;

        for (int i = 0; i < sizeBytesArr; i++) {
            byte[] inArrBytes = new byte[confFile.NumBytes()];
            for (iByte = 0; iByte < confFile.NumBytes(); iByte++)
                inArrBytes[iByte] = inBytes[iByte + iByteCur];

            byte[] outArrBytes = tabMap.GetValue(inArrBytes);
            for (iByte = 0; iByte < confFile.NumBytes(); iByte++)
                outBytes[iByte + iByteCur] = outArrBytes[iByte];

            iByteCur += iByte;
        }

        if (sizeRestBytesArr != 0) {
            for (iByte = iByteCur; iByte < inBytes.length; iByte++)
                outBytes[iByte] = inBytes[iByte];
        }
    }

    /**
     * get result
     *
     * @return coding bytes
     */
    public byte[] GetData() {
        return outBytes;
    }
}
