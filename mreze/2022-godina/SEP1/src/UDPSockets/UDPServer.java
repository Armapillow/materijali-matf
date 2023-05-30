package UDPSockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UDPServer {

    public static void main(String[] args) {

        try (DatagramSocket server = new DatagramSocket(UDPClient.PORT)) {

            System.err.println("Server started on port " + UDPClient.PORT);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                server.receive(request);

                String result = new String(request.getData(), 0, request.getLength());
                System.err.println("GOT: " + result.trim());

                String responseStr   = getResponse(result);
                byte[] responseBytes = responseStr.getBytes(StandardCharsets.US_ASCII);

                DatagramPacket response = new DatagramPacket(responseBytes, responseBytes.length,
                        request.getAddress(), request.getPort());
                server.send(response);
            }

        } catch (IOException ex) {
            System.err.println("Server error!");
            System.err.println(ex.getMessage());
        }
    }

    private static String getResponse(String result) {

        String[] fields = result.split(" ");
//        System.out.println(fields[0] + " " + fields[1]);
        int num1 = Integer.parseInt(fields[0].trim());
//        System.out.println(num1);
        int num2 = Integer.parseInt(fields[1].trim());
//        System.out.println(num2);

        String response = "";
        if (num1 < 0 || num2 < 0 || (num1 > num2)) {
            response = "Nevalidan opseg!";
        }
        else {
            int sum = (num2 * (num2 + 1) / 2) - (num1 * (num1 + 1) / 2) + num1;
            response = Integer.toString(sum);
            System.err.println("Sum: " + sum);
        }

        return response;
    }
}
