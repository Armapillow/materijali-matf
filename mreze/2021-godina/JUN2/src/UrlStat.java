import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UrlStat {

    public static void main(String[] args) {

        String urlPath = "tests/urls";
        Path path = Paths.get(urlPath).toAbsolutePath();
        try (var dirStream = Files.newDirectoryStream(path)) {

            for (Path p : dirStream) {
                if (Files.isRegularFile(p)) {

                    var urlRunnable = new UrlStatRunnable(p);
                    Thread t = new Thread(urlRunnable);
                    t.start();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
