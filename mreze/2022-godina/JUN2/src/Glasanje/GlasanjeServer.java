package Glasanje;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GlasanjeServer {
    private static final Map<String, Integer> votes = new HashMap<>();
    private static int validVotes   = 0;
    private static int invalidVotes = 0;

    public static void main(String[] args) {
        int port = 12345;

        readAllVotes();

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()
        ) {
            if (!selector.isOpen() || !serverChannel.isOpen()) {
                System.err.println("ERROR OPEN: server or selector");
                System.exit(1);
            }

            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.err.println("Server started on port " + port);

            while (true) {

                // TODO: how??
//                try {
                    selector.select(5);
//                    selector.wakeup();
//                } catch (IOException io) {
//                    System.err.println("proslo vreme za select!!!");
//                }


                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    try {
                        if (key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();

                            client.configureBlocking(false);
                            System.err.println("Accepted client from " + client);

                            client.register(selector, SelectionKey.OP_READ);

                        } else if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            if (buffer == null) {
                                buffer = ByteBuffer.allocate(1024);
//                                key.attach(buffer);
                            }

                            buffer.clear();
                            client.read(buffer);

                            String bufferString = new String(buffer.array(), 0, buffer.position()).trim();
                            System.err.println("GOT: " + bufferString);

                            String response = getResponse(bufferString);

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

                        }
                    } catch (IOException e) {
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException cex) {
                            cex.printStackTrace();
                        }
                    }
                }
            }

        } catch (IOException ex) {
            System.err.println("SERVER ERROR");
            System.err.println(ex.getMessage());
        }
    }

    private static String getResponse(String vote) {
        vote = vote.toLowerCase().trim();

        String response = "";
        if (votes.containsKey(vote)) {
            validVotes++;
            int oldVotes = votes.get(vote);
            votes.replace(vote, oldVotes+1);

            double percent = votes.get(vote) / (double)(invalidVotes + validVotes);
            percent = (double)Math.round(percent * 100) / 100;
            response = "Hvala sto ste glasali , trenutni procenat glasova za vaseg kandidata je " + percent;

        } else {
            invalidVotes++;
            response = "Vas glas nije validan";
        }

        return response;
    }

    private static void readAllVotes() {
        String path = "lista.txt";

        try (var input = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = input.readLine()) != null) {
                String vote = line.toLowerCase().trim();
                votes.put(vote, 0);
            }

//            for (var entry : votes.entrySet()) {
//                System.out.println(entry.getKey() + " -> " + entry.getValue());
//            }
            System.err.println("File read");
        } catch (IOException ex) {
            System.err.println("ERROR FILE: open");
            System.err.println(ex.getMessage());
        }
    }
}
