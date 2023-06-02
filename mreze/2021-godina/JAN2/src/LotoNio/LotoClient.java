package LotoNio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class LotoClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (SocketChannel client = SocketChannel.open(new InetSocketAddress(hostname, port));
             Scanner sc = new Scanner(System.in)
        ) {
            System.err.println("Connected to server on port " + port);

            System.out.print("Unesi loto kombinaciju [1, 39] (7 br.): ");
            String input = sc.nextLine().trim();

            client.write(ByteBuffer.wrap(input.getBytes()));

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.clear();
            client.read(buffer);

            String result = new String(buffer.array(), 0, buffer.position());
            System.out.println(result);


        } catch (IOException ex) {
            System.err.println("CLIENT error!");
            System.err.println(ex.getMessage());
        }
    }
}
