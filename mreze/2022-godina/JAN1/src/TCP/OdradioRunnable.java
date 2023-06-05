package TCP;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class OdradioRunnable implements Runnable {
    private Socket client = null;
    private String clientName;
    private final Set<String> jobs;
    private static final String pathToLog = "tests/log.txt";
    private FileWriter logScanner;

    public OdradioRunnable(Socket client) throws IOException {
        this.client = client;
        this.jobs = Collections.synchronizedSet(new HashSet<>());
        logScanner = new FileWriter(pathToLog);
    }

    @Override
    public void run() {
        try (var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             var out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))
        ) {

            this.clientName = in.readLine().trim();
            System.err.println("Ulogovan korisnik '" + clientName + "'");

            String formatDateStr = "dd.MM.yyyy HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(formatDateStr);
            while (true) {
                String komanda = in.readLine().trim();
                if (komanda.equalsIgnoreCase("izadji")) {
                    break;
                }

                String date = sdf.format(new Date());
                if (komanda.equalsIgnoreCase("odradi")) {
                    String posao = "";

                    // TODO: try-catch, size() == 0
                    synchronized (this.jobs) {
                        posao = this.jobs.iterator().next();
                        this.jobs.remove(posao);
                    }

                    out.write(posao);
                    out.newLine();
                    out.flush();

                    String logLine = date + ": Korisnik " + this.clientName + " je odradio zadatak '" + posao + "'\n";
                    logScanner.write(logLine);

                } else if (komanda.contains("dodaj")) {
                    String[] fields = komanda.split(" ");
                    String posao = fields[1].trim();
                    jobs.add(posao);
                    String logLine = date + ": Korisnik '" + clientName + "' je dodao zadatak '" + posao + "'\n";
                    logScanner.write(logLine);
                }
            }

        } catch (IOException ex) {
            System.err.printf("Client handler [%2d] errored:\n", Thread.currentThread().getId());
            System.err.println(ex.getMessage());
        } finally {
            try {
                client.close();
                logScanner.close();
                System.err.println("Client is closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.err.printf("Client handler [%2d] finished!\n", Thread.currentThread().getId());
    }
}
