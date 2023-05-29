import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CodeSearchRunnable implements Runnable {

    private final Path path;
    private final String word;
    public CodeSearchRunnable(Path p, String w) {
        this.path = p;
        this.word = w;
    }

    @Override
    public void run() {

        int numOfLines = 0;
        try (var input = Files.newBufferedReader(this.path)) {

            String line = "";
            while ((line = input.readLine()) != null) {
                numOfLines++;


                if (line.toLowerCase().contains(this.word)) {
                    int wordIndex = line.indexOf(this.word) + 1;

                    synchronized (System.out) {
                        System.out.println(
                                path.toString() + ":" + numOfLines + ":" +
                                        wordIndex + ":" + line
                        );
                    }
                }
            }

        } catch (IOException e) {
            System.err.println(this.path.toString());
            e.printStackTrace();
        }

    }
}
