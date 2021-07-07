/**
 *
 *  @author Sukhetskyi Nazarii S20363
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.*;

public class ChatClientTask implements Runnable {
    private final ChatClient client;
    private final List<String> reqs;
    private final int time;

    public ChatClientTask(ChatClient client, List<String> reqs, int time) {
        this.client = client;
        this.reqs = reqs;
        this.time = time;
    }

    public void get() throws InterruptedException, ExecutionException {
        // this.start();
    }

    public static ChatClientTask create(ChatClient client, List<String> reqs, int time) {
        return new ChatClientTask(client, reqs, time);
    }

    public ChatClient getClient() {
        return client;
    }

    @Override
    public void run() {
        try {
            client.login();
            if (time != 0) Thread.sleep(time);
            for (String req : reqs) {
                client.send(req);
                if (time != 0) Thread.sleep(time);
            }
            client.logout();
            if (time != 0) Thread.sleep(time);
        } catch (InterruptedException ignored) { }
    }
}