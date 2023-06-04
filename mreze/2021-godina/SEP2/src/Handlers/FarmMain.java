package Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class FarmMain {
    public static void main(String[] args) throws IOException {
//        String urlString = "farm://localhost:13370?q=repair&x=3&y=4";
        String urlString = "farm://localhost:13370?q=mark&x=5&y=3";
//        String urlString = "farm://localhost:13370?q=mark&x=3&y=";
//        String urlString = "farm://localhost:13370?q=mark&y=5";
//        String urlString = "farm://localhost?q=mark&y=5";

        URL url = new URL(null, urlString, new FarmHandler());
        var connection = url.openConnection();

        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
    }
}
