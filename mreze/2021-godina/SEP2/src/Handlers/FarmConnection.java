package Handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FarmConnection extends URLConnection {

    public static final int DEFAULT_PORT = 13370;
    private Socket client = null;
    public FarmConnection(URL u) {
        super(u);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!this.connected)
            connect();

        return this.client.getInputStream();
    }

    @Override
    public void connect() throws IOException {
        if (!this.connected) {

            int port = url.getPort();
            if (port < 1 || port > 65535)
                port = DEFAULT_PORT;

            this.client = new Socket(url.getHost(), port);
            OutputStream out = this.client.getOutputStream();

            String query = url.getQuery();
            System.err.println("Query: " + query);

            if (query == null || query.equalsIgnoreCase("")) {
                System.err.println("Nije kompletan upit.");
                System.exit(1);
            }

            String[] fields = query.split("&");
            if (fields.length != 3) {
                System.err.println("Nije kompletan upit.");
                System.exit(1);
            }

            System.err.println(Arrays.toString(fields));

            String upit = fields[0].substring(2).trim();
            String x    = fields[1].substring(2).trim();
            String y    = fields[2].substring(2).trim();

            String result = upit + " " + x + " " + y;
            System.err.println("Sending '" + result + "'...");

            byte[] resultBytes = result.getBytes(StandardCharsets.UTF_8);
            out.write(resultBytes);
            out.write('\r');
            out.write('\n');
            out.flush();


            this.connected = true;
        }
    }
}
