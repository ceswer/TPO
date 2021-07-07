/**
 *
 *  @author Sukhetskyi Nazarii S20363
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ChatClient extends Thread {
    private SocketChannel socketChannel;
    private final StringBuilder chatView;
    private final String id;

    public ChatClient(String host, int port, String id) {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));                                      // opening channel, connecting to the address
            socketChannel.configureBlocking(false);                                                                     // making it none-blocking
        } catch (IOException ignored) { }
        this.chatView = new StringBuilder("=== " + id + " chat view" + "\n");
        this.id = id;
    }

    public void login() {
        this.start();
        this.send("logged in");
    }

    public void logout() throws InterruptedException {
        this.send("logged out");
        Thread.sleep(100);
        this.interrupt();
    }

    public void send(String req) {
        ByteBuffer byteBuffer = Charset.forName("ISO-8859-2").encode(id + ": " + req + "STOP");                      // encoding req with encoding, because of Polish tokens
        try {
            Thread.sleep(50);
            socketChannel.write(byteBuffer);                                                                            // sending req
        } catch (IOException exception) {
            send(req);
        } catch (InterruptedException ignored) { }
    }

    public String getChatView() {
        return chatView.toString().replace("STOP", "\n");
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {                                                                                  // reading while Thread is not interrupted
                ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                StringBuilder request = new StringBuilder();

                while (socketChannel.read(byteBuffer) > 0) {
                    byteBuffer.flip();                                                                                  // making buffer ready for a new sequence
                    request.append(Charset.forName("ISO-8859-2").decode(byteBuffer));                                   // reading what is sent to that client
                    byteBuffer.clear();                                                                                 // cleaning buffer after receiving sequence
                }

                if (!request.toString().isEmpty())
                    chatView.append(request);                                                                           // appending ChatView with received sequence
            }
        } catch (IOException ignored) { }
    }
}