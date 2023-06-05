package TCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class OdradioServer {
    public static void main(String[] args) {
        int PORT = 12345;

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.err.println("Started server on port " + PORT);

            while(true) {
                System.err.println("Waiting for clients...");

                Socket client = server.accept();
                System.err.println("Accepted client " + client);
                new Thread(new OdradioRunnable(client)).start();

            }
        } catch (IOException ex) {
            System.err.println("Server error!");
            System.err.println(ex.getMessage());
        }
    }
}
