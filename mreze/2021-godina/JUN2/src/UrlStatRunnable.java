import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class UrlStatRunnable implements Runnable {

    private final Path path;

    public UrlStatRunnable(Path p) {
        this.path = p;
    }

    @Override
    public void run() {

        try (var bufRed = Files.newBufferedReader(path)) {

        String line;
        while ((line = bufRed.readLine()) != null) {
            String[] fields = line.split(" ");
            int num = Integer.parseInt(fields[0].trim());
            String urlStr = fields[1].trim();

            try {
                URL url = new URL(urlStr);
                String protocol = url.getProtocol();
                String host = url.getHost();
                String resource = url.getPath();

                boolean matrixBool = false;
                String result = "";
                if (isIP(host)) {
                    result = "(v" + getVersion(host) + ") ";
                    matrixBool = true;
                }

                result += resource + " " + protocol;
                synchronized (System.out) {
                    if (matrixBool) {
                        String[][] matrix = getMatrix(num);
                        for (int i = 0; i < num; i++) {
                            for (int j = 0; j < num; j++) {
                                System.out.print(matrix[i][j]);
                            }
                            System.out.println();
                        }
                    }
                    System.out.println(result);
                }
            } catch (MalformedURLException ex) {
                // invalid urls
//                System.err.println(urlStr);
            }
        }
    } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static boolean isIP(String host) {
        return (host.split("\\.").length == 4) ||
                (host.indexOf(':') != -1);
    }

    private static int getVersion(String host) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(host);
        byte[] bytes = address.getAddress();

        return switch(bytes.length) {
            case 4 -> 4;
            case 16 -> 6;
            default -> -1;
        };
    }

    private String[][] getMatrix(int num) {
        String[][] matrixStr = new String[num][num];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                if (i == j)
                    matrixStr[i][j] = ">";
                else
                    matrixStr[i][j] = "=";
            }
        }

        return matrixStr;
    }
}
