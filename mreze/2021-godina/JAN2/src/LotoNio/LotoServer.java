package LotoNio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class LotoServer {

    private static ArrayList<Integer> serverCombination = new ArrayList();

    public static void main(String[] args) {
        int port = 12345;
        generateServerCombination();
        System.err.println("Server combination: ");
//        for (var c : serverCombination) {
//            System.err.print(c + " ");
//        }
        System.err.println(serverCombination);

        try (ServerSocketChannel serverChanel = ServerSocketChannel.open();
             Selector selector = Selector.open()
        ) {
            if (!selector.isOpen() || !serverChanel.isOpen()) {
                System.err.println("Error open: server or selector");
                System.exit(1);
            }

            serverChanel.bind(new InetSocketAddress(port));
            serverChanel.configureBlocking(false);
            serverChanel.register(selector, SelectionKey.OP_ACCEPT);

            System.err.println("Server started on port " + port);

            while (true) {
                selector.select();

                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    try {
                        if (key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();

                            client.configureBlocking(false);
                            System.err.println("Accepted client " + client.getRemoteAddress());

                            client.register(selector, SelectionKey.OP_READ);

                        } else if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            if (buffer == null) {
                                buffer = ByteBuffer.allocate(1024);
                                key.attach(buffer);
                            }

                            buffer.clear();
                            client.read(buffer);

                            String result = new String(buffer.array(), 0, buffer.position()).trim();
                            System.err.println("  GOT: " + result);

                            String response = generateResponse(result);


                            ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
                            responseBuffer.put(response.getBytes());

                            key.attach(responseBuffer);
                            key.interestOps(SelectionKey.OP_WRITE);

                        } else if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            buffer.flip();
                            client.write(buffer);
                            if (!buffer.hasRemaining()) {
                                System.err.println("Finished working with client...");
                                client.close();
                            }

//                            key.interestOps(SelectionKey.OP_READ);
                        }

                    } catch (IOException e) {
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException ce)  {
                            ce.printStackTrace();
                        }
                    }
                }
            }

        } catch (IOException ex) {
            System.err.println("Server error");
            System.err.println(ex.getMessage());
        }
    }

    private static String generateResponse(String combString) {
        combString = combString.trim();
        String[] fields = combString.split(" ");
        ArrayList<Integer> clientCombination = new ArrayList<>();

        for (String f : fields)
            clientCombination.add(Integer.parseInt(f));

        int count = 0, n = serverCombination.size();
        for (int i = 0; i < n; i++) {
            int num = clientCombination.get(i);
            if (serverCombination.contains(num))
                count++;
        }

        return String.valueOf(count);
    }

    private static void generateServerCombination() {
        ArrayList<Integer> combinations = new ArrayList<>();

        for (int i = 1; i <= 39; i++)
            combinations.add(i);

        Collections.shuffle(combinations);

        for (int i = 0; i < 7; i++) {
            serverCombination.add(combinations.get(i));
        }
    }
}
