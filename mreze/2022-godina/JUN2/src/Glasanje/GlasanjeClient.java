package Glasanje;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class GlasanjeClient {

    public static void main(String[] args) {
        String hostname = "localhost";
        int    port = 12345;

        try (SocketChannel client = SocketChannel.open(new InetSocketAddress(hostname, port));
             Scanner sc = new Scanner(System.in)
        ) {
            System.err.println("Connected to server on port " + port);

            System.out.print("Unesi glas: ");
            String line = sc.nextLine().trim();

            client.write(ByteBuffer.wrap(line.getBytes()));

            ByteBuffer response = ByteBuffer.allocate(1024);
            client.read(response);

            String result = new String(response.array(), 0, response.position());
            System.out.println(result);

        } catch (IOException ex) {
            System.err.println("Client error!");
            System.err.println(ex.getMessage());
        }

    }
}
