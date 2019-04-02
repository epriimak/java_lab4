import java.io.IOException;

/**
 * class my thread
 */
public class MyThread extends Thread {
    private int startPos, sizeBlock, inc;
    private String algConfigName;
    private int curPos = 0;

    private BlockController bContr;
    private Writer writer;

    /**
     * init MyThread
     *
     * @param bContr            block controller
     * @param writer            writer
     * @param fConfigThreadName name of config file
     * @param algConfName       name of config file of algorithm
     * @throws IOException                if files not exist
     * @throws ParseThreadConfigException if config file of thread is not correct
     */
    public MyThread(Writer writer, BlockController bContr, String fConfigThreadName, String algConfName) throws IOException, ParseThreadConfigException {
        ParseThreadConfig threadConfig = new ParseThreadConfig(fConfigThreadName);
        startPos = threadConfig.GetStartPos();
        sizeBlock = threadConfig.GetBlockSize();
        inc = threadConfig.GetInc();
        algConfigName = algConfName;

        this.bContr = bContr;
        this.writer = writer;
    }


    @Override
    public void run() {
        byte[] myBytes = null;
        Coding algCode = null;

        try {
            algCode = new Coding(algConfigName);
        } catch (IOException | ParseCodingConfigException | TableException e) {
            e.printStackTrace();
        }

        curPos = startPos;

        while (bContr.NotEnd(sizeBlock, curPos)) {
            try {
                synchronized (bContr) {
                    myBytes = bContr.GetBlockBytes(sizeBlock, curPos);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (myBytes != null) {
                algCode.Run(myBytes);
                try {
                    writer.putResult(algCode.GetData(), curPos, sizeBlock);
                    curPos += inc;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
