package UDPSockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {

    private static final String hostname = "localhost";
    private static final int    port     = 31415;
    private static final int    BUF_SIZE = 1024;

    public static void main(String[] args) {

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress iAddress = InetAddress.getByName(hostname);

            Scanner sc = new Scanner(System.in);
            System.out.print("Unesi realan broj: ");
            String inputLine = sc.nextLine().trim();
            sc.close();

            byte[] bytes = inputLine.getBytes(StandardCharsets.UTF_8);
            // send
            DatagramPacket request = new DatagramPacket(bytes, bytes.length, iAddress, port);
            clientSocket.send(request);

            // receive
            DatagramPacket response = new DatagramPacket(new byte[BUF_SIZE], BUF_SIZE);
            clientSocket.receive(response);

            String result = new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8);
            System.out.println("Povrsina: " + result);

        } catch (IOException ex) {
            System.err.println("CLIENT ERROR!");
            System.err.println(ex.getMessage());
        }

    }
}
