package Menjacnica;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class MenjacnicaClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (SocketChannel client = SocketChannel.open(new InetSocketAddress(hostname, port));
             Scanner sc = new Scanner(System.in)
        ) {
            System.err.println("Connected to the server...");

            System.out.println("Unesi valutu i iznos za konverziju (prekid)");
            while (true) {
                String line = sc.nextLine().trim();

                client.write(ByteBuffer.wrap(line.getBytes()));
                if (line.equalsIgnoreCase("prekid"))
                    break;

                ByteBuffer response = ByteBuffer.allocate(1024);
                response.clear();
                client.read(response);

                String result = new String(response.array(), 0, response.position()).trim();
                System.out.println(result);
            }

        } catch (IOException ex) {
            System.err.println("Client error");
            System.err.println(ex.getMessage());
        }
    }
}
