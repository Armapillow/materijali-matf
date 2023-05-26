import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.Arrays;

public class LogParser {


    public static void main(String[] args) {

        String logPath = "src/logfile.txt";
        String absolutePath = Paths.get(logPath).toAbsolutePath().toString();

        try {
            URL fileURL = new URL("file://" + absolutePath);
            URLConnection urlConnection = fileURL.openConnection();

            BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

//            input.lines().forEach(System.out::println);
//            new BufferedReader(new InputStreamReader(urlConnection.getInputStream())).lines()
//                    .forEach(System.out::println);

            while ((line = input.readLine()) != null) {

                if (line.contains("https") || line.contains("http")) {
                    processLine(line);
                }
            }


        } catch (IOException ex) {
            System.err.println("Nije mogao da otvori fajl na datoj putanji!");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }

    }

    private static void processLine(String line) {

        String[] fields = line.split("]");
        String date = fields[0].substring(1).trim();
        String ipAddress = fields[1].substring(1).trim();
        String urlString = fields[2].substring(1).trim();

//        System.out.println(date + "\n" + ipAddress + "\n" + urlString);

        try {
            URL url = new URL(urlString);
            String protocol = url.getProtocol().trim();
            String path = url.getPath().trim();

            String result = "v" + getIPVersion(ipAddress)
                          + ":" + protocol + ":" + path;

            System.out.println(result);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (UnknownHostException e)  {
            e.printStackTrace();
        }
    }

    private static int getIPVersion(String ipStr) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ipStr);
        byte[] bytes = address.getAddress();

        return switch (bytes.length) {
            case 4 -> 4;
            case 16 -> 6;
            default -> -1;
        };
    }
}
