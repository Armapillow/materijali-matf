package PogodiBrojTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServer {
    public static void main(String[] args) {
        int port = 12321;

        try (ServerSocket server = new ServerSocket(port)) {
            System.err.println("Started server on port " + port);

            while (true) {
                System.err.println("Waiting for clients..");

                Socket client = server.accept();
                System.err.println("Accepted client " + client);

                new Thread(new TCPRunnable(client)).start();
            }

        } catch (IOException ex) {
            System.err.println("Server error!");
            System.err.println(ex.getMessage());
        }
    }
}
