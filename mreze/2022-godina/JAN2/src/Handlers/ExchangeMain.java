package Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ExchangeMain {

    public static void main(String[] args) throws IOException {
        String urlString = "exchange://localhost:12345?valuta=EUR&iznos=5";

        URL url   = new URL(null, urlString, new ExchangeHandler());
        var conn  = url.openConnection();

        try (var input = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
