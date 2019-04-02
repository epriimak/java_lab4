import java.io.IOException;

/**
 * class that run threads
 */
public class ThreadController {
    private static int numThread;
    private String algConfigName;
    private static final String nameThreadConfig = "thread";
    private static BlockController bContr;
    private static Writer writer;
    private static MyThread[] myThreads;
    private String fInName, fMainConfigName, fOutName;
    private String[] namesThreadsConfig;
    /**
     * init class
     *
     * @param args args from cmd
     * @throws ParseCmdExeption         if cmd args is not correct
     * @throws IOException              if files is not exist
     * @throws ParseMainConfigException if main config file is not correct
     */
    ThreadController(String args[]) throws ParseCmdExeption, IOException, ParseMainConfigException {
        ParseCmd parseCmd = new ParseCmd(args);
        fInName = parseCmd.GetFInName();
        fMainConfigName = parseCmd.GetFMainConfigName();
        fOutName = parseCmd.GetFOutName();

        ParseMainConfig parseMainConfig = new ParseMainConfig(fMainConfigName);
        numThread = parseMainConfig.GetNumThreads();

        bContr = new BlockController(parseMainConfig.GetBlockSize(), fInName);
        algConfigName = parseMainConfig.GetFAlfConfigName();

        writer = new Writer(fOutName);

        namesThreadsConfig = new String[numThread];
    }

    /**
     * set names thread's config file
     */
    private void SetNamesThreadConfig(){
        for(int i = 1; i <= numThread; i++){
            namesThreadsConfig[i - 1] = nameThreadConfig + i + ".txt";
        }
    }

    /**
     * start threads
     *
     * @throws IOException                if output file is
     * @throws ParseThreadConfigException if config threads is not correct
     */

    public void Run() throws IOException, ParseThreadConfigException, InterruptedException {
        SetNamesThreadConfig();
        bContr.GetBytesFromFile(0);
        myThreads = new MyThread[numThread];
        for (int i = 0; i < numThread; i++) {
            myThreads[i] = new MyThread(writer, bContr, namesThreadsConfig[i], algConfigName);
            myThreads[i].start();
        }

        for(int i = 0; i < numThread; i++)
            myThreads[i].join();

        writer.writeResults();
    }
}
