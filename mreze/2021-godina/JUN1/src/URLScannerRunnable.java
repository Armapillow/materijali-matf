import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class URLScannerRunnable implements Runnable {

    private final Path path;
    public URLScannerRunnable(Path p) {
        this.path = p;
    }

    @Override
    public void run() {

        try (BufferedReader input = Files.newBufferedReader(this.path)) {

            String line;
            while ((line = input.readLine()) != null) {
                String urlString = line.trim();

                try {
                    URL url = new URL(urlString);

                    String protocol = url.getProtocol();
                    String auth     = url.getAuthority();
                    String path     = url.getPath();
                    String host     = url.getHost();

                    String result = "";
                    String bytesStr = "";
                    if (isIP(host)) {
                        result += "(v" + getVersion(host) + ") ";
                        bytesStr = Arrays.toString(host.split("\\."));
                    }

                    result += protocol + " " + auth + " " + path + " " + bytesStr;

                    synchronized (System.out) {
                        System.out.println(result);
                    }

                } catch (MalformedURLException e) {
//                    System.err.println("URL nije podrzan! " + urlString);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getVersion(String host) throws UnknownHostException {

        InetAddress inetAddress = InetAddress.getByName(host);
        byte[] bytes = inetAddress.getAddress();

        return switch(bytes.length) {
            case 4 -> 4;
            case 16 -> 6;
            default -> -1;
        };

    }
    private static boolean isIP(String host) {
        return host.split("\\.").length == 4 || host.split(":").length == 6;
    }
}
