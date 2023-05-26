import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class URLScanner {

    public static void main(String[] args) {
        String dirPath = "tests/urls";

        try (var dirs = Files.newDirectoryStream(Paths.get(dirPath).toAbsolutePath())) {

            for (Path p : dirs) {
                if (Files.isRegularFile(p)) {
                    var urlRunnable = new URLScannerRunnable(p);
                    Thread t = new Thread(urlRunnable);
                    t.start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
