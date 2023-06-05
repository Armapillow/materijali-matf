package PogodiBrojTCP;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class TCPRunnable implements Runnable {

    private Socket client = null;
    private final int NUM;
    public TCPRunnable(Socket client) {
        this.client = client;
        NUM = new Random().nextInt(100) + 1;
    }

    @Override
    public void run() {
        String greetingMessage = "Pogodi koji broj od 1 do 100 sam zamisilo";

        try (var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             var out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))
        ) {
            out.write(greetingMessage);
            out.newLine();
            out.flush();

            while (true) {
                String line = in.readLine().trim();
                int clientNUM = Integer.parseInt(line);

                if (clientNUM < NUM) {
                    out.write("Zamisljeni broj je veci od toga");
                    out.newLine();
                    out.flush();

                } else if (clientNUM > NUM) {
                    out.write("Zamisljeni broj je manji od toga");
                    out.newLine();
                    out.flush();
                } else {
                    out.write("Cestitam! Pogodili ste broj!");
                    out.newLine();
                    out.flush();
                    break;

                }
            }

        } catch (IOException e) {
            System.err.printf("Error in client hander [%2d]:\n", Thread.currentThread().threadId());
            System.err.println(e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.err.printf("Client handler [%2d] finished!\n", Thread.currentThread().threadId());
    }
}
