import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Poezija {

    public static void main(String[] args) {

        String path = "tests/pesme";

        Scanner input = new Scanner(System.in);
        System.out.print("Unesi rec za trazenje: ");
        String rec = input.nextLine().trim();
        input.close();

        try (var dirStream = Files.newDirectoryStream(Paths.get(path).toAbsolutePath())) {

            for (var p : dirStream) {
                if (Files.isRegularFile(p)) {
                    var poezijaRunnable = new PoezijaRunnable(p, rec);
                    Thread t = new Thread(poezijaRunnable);
                    t.start();
                }
            }

        } catch (IOException e) {
            System.err.println("Directory!");
            e.printStackTrace();
        }
    }
}
