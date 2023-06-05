package PogodiBrojTCP;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12321;

        try (Socket client = new Socket(hostname, port);
             var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             var out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
             Scanner sc = new Scanner(System.in)
        ) {
            System.err.println("Connected to server...");
            String greetingMess = in.readLine().trim();
            System.out.println(greetingMess);

            while (true) {
                String guess = sc.nextLine().trim();
                out.write(guess);
                out.newLine();
                out.flush();

                String response = in.readLine();
                System.out.println(response);
                if (response.toLowerCase().contains("pogodili"))
                    break;
            }

        } catch (IOException e) {
            System.err.println("Client error!");
            System.err.println(e.getMessage());
        }
    }
}
