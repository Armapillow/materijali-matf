package Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class QuizMain {
    public static void main(String[] args) throws IOException {
//        String urlString = "quiz://localhost";
//        String urlString = "quiz://localhost:12345?oblast=Geografija";
//        String urlString = "quiz://localhost:7337?oblast=Geografija";
        String urlString = "quiz://localhost:12345?oblast=x";

        URL url = new URL(null, urlString, new QuizHandler());
        var conn = url.openConnection();

        BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
    }
}
