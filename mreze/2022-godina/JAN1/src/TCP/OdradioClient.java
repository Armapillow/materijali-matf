package TCP;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class OdradioClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (Socket client = new Socket(hostname, port);
             var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             var out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
             var stdin = new Scanner(System.in)
        ) {
            System.err.println("Connected to server on port " + port);
            System.out.print("Predstavi se (ime): ");
            String ime = stdin.nextLine().trim();

            out.write(ime);
            out.newLine();
            out.flush();

            System.out.println("Unesi komande (odradi, dodaj, izadji):");
            while (true) {
                String komanda = stdin.nextLine().trim();
                out.write(komanda);
                out.newLine();
                out.flush();
                if (komanda.equalsIgnoreCase("izadji")) {
                    break;
                } else if (komanda.equalsIgnoreCase("odradi")) {
                    String result = in.readLine().trim();
                    System.out.println("Vas zadatak je: " + result);
                }
            }

        } catch (IOException ex) {
            System.err.println("Client error");
            System.err.println(ex.getMessage());
        }
    }
}
