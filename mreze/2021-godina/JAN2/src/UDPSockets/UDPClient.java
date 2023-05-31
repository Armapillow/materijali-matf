package UDPSockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int    port     = 12345;

        try (DatagramSocket clientSocket = new DatagramSocket();
             Scanner sc = new Scanner(System.in)
        ) {
            InetAddress iAddress = InetAddress.getByName(hostname);

            System.out.print("Unesi liniju: ");
            String inputLine = sc.nextLine().trim();

            // send
            byte[] inputLineBytes = inputLine.getBytes(StandardCharsets.UTF_8);
            DatagramPacket request = new DatagramPacket(inputLineBytes, inputLineBytes.length,
                    iAddress, port);
            clientSocket.send(request);

            // receive
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
            clientSocket.receive(response);

            String result = new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8);
            System.out.println(result);

        } catch (IOException ex) {
            System.err.println("Client error!");
            System.err.println(ex.getMessage());
        }
    }
}
