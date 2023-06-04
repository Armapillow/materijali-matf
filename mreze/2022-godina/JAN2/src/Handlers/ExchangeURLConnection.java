package Handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ExchangeURLConnection extends URLConnection {

    private Socket client = null;
    public static final int PORT = 12345;

    public ExchangeURLConnection(URL u) {
        super(u);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!this.connected)
            connect();

        return this.client.getInputStream();
    }

    @Override
    public synchronized void connect() throws IOException {
        if (!this.connected) {
            int port = url.getPort();
            if (port < 1 || port > 65535)
                port = PORT;

            this.client = new Socket(url.getHost(), port);
            OutputStream out = this.client.getOutputStream();

            String query = url.getQuery();
            System.err.println("Query: " + query);

            if (query == null || query.equalsIgnoreCase("")) {
                System.err.println("Upit nije vazeci!");
                System.exit(1);
            }

            String[] fields = query.split("&");
            if (fields.length != 2) {
                System.err.println("Upit nije vazeci!");
                System.exit(1);
            }
            System.err.println("Fields: " + Arrays.toString(fields));

            String curr  = fields[0].trim().substring(fields[0].indexOf('=') + 1);
            String value = fields[1].substring(fields[1].indexOf('=') + 1).trim();
            String result = curr + " " + value;
            System.err.print("Sending '" + result + "'\n");

            // FIXME: ne radi kako treba (ceka)
            byte[] resultBytes = result.getBytes(StandardCharsets.UTF_8);
            out.write(resultBytes);
            out.write('\r');
            out.write('\n');
            out.flush();

            this.connected = true;
        }

    }
}
