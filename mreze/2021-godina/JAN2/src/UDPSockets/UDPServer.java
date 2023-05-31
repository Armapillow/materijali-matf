package UDPSockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UDPServer {

    public static void main(String[] args) {
        int buf_size = 1024;
        int port     = 12345;

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.err.println("Started server on port " + port + "\n");

            while (true) {

                // receive
                DatagramPacket request = new DatagramPacket(new byte[buf_size], buf_size);
                serverSocket.receive(request);

                String result = new String(request.getData(), 0, request.getLength());
                System.err.println("GOT: " + result.trim());

                String responseStr   = getResponse(result);
                byte[] responseBytes = responseStr.getBytes(StandardCharsets.UTF_8);

                //send
                System.err.println("Sending data back to client...\n");
                DatagramPacket response = new DatagramPacket(responseBytes, responseBytes.length,
                        request.getAddress(), request.getPort());
                serverSocket.send(response);
            }

        } catch (IOException e) {
            System.err.println("Server error!");
            System.err.println(e.getMessage());
        }
    }

    private static String getResponse(String result) {
        int n = result.length();
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = result.charAt(i);
            if (Character.isUpperCase(c)) {
                response.append(Character.toLowerCase(c))
                        .append(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                response.append(Character.toUpperCase(c));
            } else if (Character.isDigit(c)) {
                response.append("..");
            } else
                response.append(c);
        }
        return response.toString();
    }
}
