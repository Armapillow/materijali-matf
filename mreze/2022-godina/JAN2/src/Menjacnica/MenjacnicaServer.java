package Menjacnica;

import java.io.BufferedReader;
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

public class MenjacnicaServer {
    public static void main(String[] args) {
        int port = 12345;
        Map<String, Double> currencies = new HashMap<>();

        readCurrencies(currencies);

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()
        ) {
            if (!selector.isOpen() || !serverChannel.isOpen()) {
                System.err.println("Error OPEN: server or selector!");
                System.exit(1);
            }

            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

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
                            System.err.println("Accepted client " + client);

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

                            String bufferStr = new String(buffer.array(), 0, buffer.position()).trim();
                            System.err.println("GOT: " + bufferStr.trim());

                            String response = "";
                            if (!bufferStr.equalsIgnoreCase("prekid")) {
                                response = calculateValue(currencies, bufferStr);
                            } else {
                                response = bufferStr;
                            }

                            ByteBuffer resultBuffer = ByteBuffer.allocate(1024);
                            resultBuffer.put(response.getBytes());
                            key.attach(resultBuffer);

                            key.interestOps(SelectionKey.OP_WRITE);

                        } else if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();

                            String response = new String(buffer.array(), 0, buffer.position()).trim();
                            if (response.equalsIgnoreCase("prekid")) {
                                System.err.println("Finished working with client..");
                                client.close();
                            } else {
                                buffer.flip();
                                client.write(buffer);
                                key.interestOps(SelectionKey.OP_READ);
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
            System.err.println("SERVER ERROR!");
            System.err.println(ex.getMessage());
        }
    }

    private static String calculateValue(Map<String, Double> currencies, String bufferStr) {
        String[] fields = bufferStr.split(" ");
        String curr = fields[0].toLowerCase().trim();
        double num = Double.parseDouble(fields[1].trim());

        String response = "";
        if (!currencies.containsKey(curr)) {
            response = "Ne menjamo trazenu valutu";
        } else if (num < 0) {
            response = "Iznos novca ne moze biti negativan broj";
        } else {
            response = Double.toString(num * currencies.get(curr));
        }

        return response;
    }

    private static void readCurrencies(Map<String, Double> currencies) {
        String path = "tests/kursna_lista.txt";

        try (BufferedReader input = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = input.readLine()) != null) {
                String[] fields = line.split(",");
                String curr = fields[0].trim().toLowerCase();
                Double value = Double.parseDouble(fields[1].trim());
                currencies.put(curr, value);
            }

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }
}
