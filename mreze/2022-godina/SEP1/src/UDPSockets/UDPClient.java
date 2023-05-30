package UDPSockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {

    private static final String hostname = "localhost";
    private static final int    BUF_SIZE = 1024;
    public  static final int    PORT     = 12345;

    public static void main(String[] args) {

        try (DatagramSocket clientSocket = new DatagramSocket()) {

            String inputLine;
            Scanner sc = new Scanner(System.in);
            System.out.print("Unesi dva prirodna broja: ");
            inputLine = sc.nextLine().trim();

            InetAddress iAddress = InetAddress.getByName(hostname);
            byte[] bytes = inputLine.getBytes(StandardCharsets.US_ASCII);

            DatagramPacket request = new DatagramPacket(bytes, bytes.length, iAddress, PORT);
            clientSocket.send(request);

            DatagramPacket response = new DatagramPacket(new byte[BUF_SIZE], BUF_SIZE);
            clientSocket.receive(response);

            String result = new String(response.getData(), 0, response.getLength(), StandardCharsets.US_ASCII);
            System.err.println(result);


            sc.close();

        } catch (IOException e) {
            System.err.println("Client error!");
            System.err.println(e.getMessage());
        }

    }
}
