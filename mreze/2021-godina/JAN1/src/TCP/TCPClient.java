package TCP;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (Socket client = new Socket(hostname, port);
             var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             var out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
             Scanner sc = new Scanner(System.in)
        ) {
            System.err.println("Connected to server...");

            // FIXME: ne radi kako treba, server radi sa `nc` komandom
            String poruka = in.readLine().trim();
            System.out.println(poruka);
            while (true) {
                // ispis table
                String tabla;
                while ((tabla = in.readLine()) != null) {
                    System.out.println(tabla);
                }

                System.out.print("> ");
                String potez = sc.nextLine().trim();
                out.write(potez);
                out.newLine();
                out.flush();
            }


        } catch (IOException ex) {
            System.err.println("Error client!");
            System.err.println(ex.getMessage());
        }
    }
}
