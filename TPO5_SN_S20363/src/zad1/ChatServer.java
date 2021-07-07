/**
 *
 *  @author Sukhetskyi Nazarii S20363
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;


public class ChatServer extends Thread {
    private static final HashMap<String, SocketChannel> clients = new HashMap<>();
    private ServerSocketChannel serverSocketChannel;
    private final StringBuilder serverLog;
    private Selector selector;

    public ChatServer(String host, int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();                                                           // opening channel
            serverSocketChannel.socket().bind(new InetSocketAddress(host, port));                                       // binding related address (localhost, port) to which clients will be connected
            serverSocketChannel.configureBlocking(false);                                                               // none-blocking channel

            selector = Selector.open();                                                                                 // Selector declaration
            serverSocketChannel.register(selector, serverSocketChannel.validOps());                                     // registering selector
        } catch (IOException ignored) { }
        serverLog = new StringBuilder();
    }

    public void startServer() {
        this.start();
        System.out.println("Server started" + "\n");
    }

    public void stopServer() throws InterruptedException {
        Thread.sleep(5000);                                                                                        // making Thread sleep, so all reqs will have come by interruption moment
        this.interrupt();
        System.out.println("Server stopped");
    }

    public String getServerLog() {
        return String.valueOf(serverLog).replace("STOP", "\n");
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {                                                                           // sent SYN, has issued SYN+ACK
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);                                                         // none-blocking channel
                        socketChannel.register(selector, SelectionKey.OP_READ);                                         // registering selector, OP_READ - you should then read
                    }

                    if (key.isReadable()) {                                                                             // there is data in the socket receives buffer
                        SelectableChannel selectableChannel = key.channel();
                        SocketChannel socketChannel = (SocketChannel) selectableChannel;

                        if (socketChannel.isOpen()) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                            StringBuilder request = new StringBuilder();

                            while (socketChannel.read(byteBuffer) > 0) {                                                // reading
                                byteBuffer.flip();
                                request.append(Charset.forName("ISO-8859-2").decode(byteBuffer));                       // decoding
                                byteBuffer.clear();
                            }

                            if (request.toString().contains("logged in"))                                               // adding client if logging in
                                clients.put(request.substring(0, request.indexOf(":")), socketChannel);

                            if (request.toString().contains("logged out")) {                                            // removing client if logging out
                                String ID = request.substring(0, request.toString().indexOf(":"));                      // sending req that client logged out
                                clients.get(ID).write(Charset.forName("ISO-8859-2").encode(request.toString().replace(": logged", " logged")));
                                clients.remove(ID);
                            }

                            request = new StringBuilder(request.toString().replace(": logged", " logged"));

                            if (!request.toString().isEmpty()) {
                                serverLog.append(new SimpleDateFormat("HH:mm:ss.SSS").format(System.currentTimeMillis())).append(" ").append(request);

                                for (Map.Entry<String, SocketChannel> entry : clients.entrySet())
                                    entry.getValue().write(Charset.forName("ISO-8859-2").encode(request.toString()));   // sending request to all clients who are logged in
                            }
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException ignored) { }
    }
}