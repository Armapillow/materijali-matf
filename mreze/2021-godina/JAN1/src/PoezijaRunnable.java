import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class PoezijaRunnable implements Runnable {

    private final String word;
    private final Path path;

    private String name;
    private int numOfWord = 0;

    public PoezijaRunnable(Path p, String rec) {
        this.path = p;
        this.word = rec;
    }

    @Override
    public void run() {

        try (BufferedReader bufferedReader = Files.newBufferedReader(this.path)) {

            String pathStr = path.toString();
            name = pathStr.substring(pathStr.lastIndexOf('/')+1, pathStr.lastIndexOf('.'));


            String line, longestLine = "";
            int numLongestLine = -1;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
//                numOfWord += Collections.frequency(Arrays.asList(line.split(" ")), word);
                if (line.toLowerCase().contains(this.word)) {
                    numOfWord++;
                }

                if (numLongestLine < line.length()) {
                    numLongestLine = line.length();
                    longestLine = line;
                }

            }

            synchronized (System.out) {
                System.out.println(name + "\n" + longestLine + "\n" + numOfWord);
            }

        } catch (IOException e) {
            System.err.println("FILE!");
            e.printStackTrace();
        }

    }
}
