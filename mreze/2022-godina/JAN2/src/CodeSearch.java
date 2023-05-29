import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CodeSearch {

    private static int numOfFiles = 0;
    private static String rec;

    public static void main(String[] args) {

        String testPath = "tests";
        Path path = Paths.get(testPath);

        Scanner sc = new Scanner(System.in);
        System.out.print("Unesi rec za pretragu: ");
        rec = sc.nextLine().trim();
        sc.close();


        dirWalk(path);
//        System.out.println("\nBroj fajlova je: " + numOfFiles);
        // 101 kad se sve racuna
        // 45 kad nema png, svg, ico ekstenzija
    }

    private static void dirWalk(Path path) {

        try (DirectoryStream<Path> dir = Files.newDirectoryStream(path)) {

            for (Path p : dir) {
                if (Files.isDirectory(p))
                    dirWalk(p);
                else if (!p.toString().endsWith(".png") && !p.toString().endsWith(".svg")
                        && !p.toString().endsWith(".ico") && !p.toString().endsWith(".gif")
                        && !p.toString().endsWith(".ttf") && Files.isRegularFile(p)) {

                    // Pozovi nit
//                    numOfFiles++;
                    Runnable codeRunnable = new CodeSearchRunnable(p, rec);
                    Thread t = new Thread(codeRunnable);
                    t.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
