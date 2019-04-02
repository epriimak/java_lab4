import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ThreadController tc = null;
        try {
            tc = new ThreadController(args);
        } catch (ParseCmdExeption | IOException | ParseMainConfigException e) {
            e.printStackTrace();
        }
        try {
            tc.Run();
        } catch (IOException | ParseThreadConfigException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

