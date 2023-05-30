package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UDPServer {

    public static void main(String[] args) {
        int bufSize = 1024;
        int port    = 12345;

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.err.println("Server started on port " + port);
            byte[] bufferFilename = new byte[bufSize];
            byte[] bufferNumber   = new byte[bufSize];

            while (true) {

                // receive x2
                DatagramPacket filenameRequest = new DatagramPacket(bufferFilename, bufferFilename.length);
                serverSocket.receive(filenameRequest);
                DatagramPacket numLineRequest  = new DatagramPacket(bufferNumber, bufferNumber.length);
                serverSocket.receive(numLineRequest);

                
                String filename = new String(filenameRequest.getData(), 0,
                                             filenameRequest.getLength(), StandardCharsets.UTF_8);
                String numLine  = new String(numLineRequest.getData(), 0,
                                             numLineRequest.getLength(), StandardCharsets.UTF_8);
                System.err.println("GOT: " + filename.trim() + "\n\t " + numLine.trim());


                String responseStr = getResponse(filename, numLine);


                // send
                byte[] responseBytes    = responseStr.getBytes(StandardCharsets.UTF_8);
                DatagramPacket response = new DatagramPacket(
                        responseBytes, responseBytes.length,
                        filenameRequest.getAddress(), filenameRequest.getPort());
                serverSocket.send(response);
            }

        } catch (IOException ex) {
            System.err.println("Error server!");
            System.err.println(ex.getMessage());
        }
    }

    private static String getResponse(String filename, String numLine) {
        StringBuilder response = new StringBuilder();

        try (var input = Files.newBufferedReader(Paths.get(filename))) {
            int lineNum = Integer.parseInt(numLine);
            String line = "";
            int count = 0;

            while ((line = input.readLine()) != null) {
                count++;
                if (count == lineNum)
                    break;
            }

//            int n = line.length();
//            for (int i = 0; i < n; i++)
//                response.append(line.charAt(n-i-1));

            response.append(line).reverse();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return response.toString();
    }
}
