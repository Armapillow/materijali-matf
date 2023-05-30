package UDP;

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
        int    BUF_SIZE = 1024;

        try (DatagramSocket client = new DatagramSocket()) {
            InetAddress iAddress = InetAddress.getByName(hostname);

            Scanner sc = new Scanner(System.in);
            System.out.print("Unesi putanju fajla: ");
            String filename = sc.nextLine().trim();
            System.out.print("Unesi broj linije: ");
            String numLine = sc.nextLine().trim();
            sc.close();

//            System.out.println(filename + "\n" + numLine);

            // send
            byte[] filenameBytes = filename.getBytes(StandardCharsets.UTF_8);
            DatagramPacket filenameRequest = new DatagramPacket(filenameBytes, filenameBytes.length,
                    iAddress, port);
            client.send(filenameRequest);

            byte[] numLineBytes = numLine.getBytes(StandardCharsets.UTF_8);
            DatagramPacket numLineRequest = new DatagramPacket(
                    numLineBytes, numLineBytes.length, iAddress, port
            );
            client.send(numLineRequest);

            // receive
            DatagramPacket response = new DatagramPacket(new byte[BUF_SIZE], BUF_SIZE);
            client.receive(response);

            String result = new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8);
            System.out.println(result);

        } catch (IOException ex) {
            System.err.println("Error client");
            System.err.println(ex.getMessage());
        }
    }
}
