package UDPSockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UDPServer {

    public static void main(String[] args) {
        int bufSize = 1024;
        int PORT    = 31415;

        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.err.println("Server started on port " + PORT + "\n");

            while (true) {
                // receive
                DatagramPacket request = new DatagramPacket(new byte[bufSize], bufSize);
                serverSocket.receive(request);

                String result = new String(
                        request.getData(), 0,
                        request.getLength(), StandardCharsets.UTF_8
                );
                System.err.println("GOT: " + result.trim());

                String responseStr = getResponse(result.trim());

                // send
                DatagramPacket response = new DatagramPacket(responseStr.getBytes(), responseStr.getBytes().length,
                        request.getAddress(), request.getPort());
                System.err.println("Sending data to client...\n");
                serverSocket.send(response);
            }
        } catch (IOException e) {
            System.err.println("SERVER ERROR!");
            System.err.println(e.getMessage());
        }
    }

    private static String getResponse(String numStr) {
        double num = Double.parseDouble(numStr);

        String response = "";
        if (num < 0)
            response = "Neispravan poluprecnik!";
        else {
            double area = Math.round((num*num * Math.PI)*10e4)/10e4;
            response    = Double.toString(area);
        }

        return response;
    }
}
