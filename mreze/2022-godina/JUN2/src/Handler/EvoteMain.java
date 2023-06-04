package Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class EvoteMain {
    public static void main(String[] args) throws IOException {
//        String urlStr = "evote://localhost:12345?kandidat=Pera+";
//        String urlStr = "evote://localhost";
//        String urlStr = "evote://localhost?test=5";
        String urlStr = "evote://localhost?kandidat=Marko";

        URL url = new URL(null, urlStr, new EvoteHandler());
        var conn = url.openConnection();

        BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
    }
}
