package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {

    public static void main(String[] args) {

        try (DatagramSocket client = new DatagramSocket()) {
            String hostname = "localhost";
            InetAddress iAddress = InetAddress.getByName(hostname);

            System.out.print("Unesi indeks i komandu: ");
            Scanner sc = new Scanner(System.in);
            String inputLine = sc.nextLine().trim();
            sc.close();

            byte[] bytes = inputLine.getBytes();
            DatagramPacket request = new DatagramPacket(bytes, bytes.length, iAddress, UDPServer.PORT);
            client.send(request);

            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
            client.receive(response);

            String result = new String(response.getData(), 0, response.getLength(), StandardCharsets.US_ASCII);
            System.out.println(result);

        } catch (IOException ex) {
            System.err.println("Client error!");
            System.err.println(ex.getMessage());
        }

    }
}
