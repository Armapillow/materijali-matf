package Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class EvoteConnection extends URLConnection {

    private Socket client = null;
    public static int PORT = 12345;
    public EvoteConnection(URL u) {
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
            OutputStream out = client.getOutputStream();

            String query = url.getQuery();
            System.err.println("Query: " + query);
            if (query == null || !query.contains("kandidat")) {
                System.err.println("Vas upit nije kompltan!");
                System.exit(1);
            }
            String nameSurname = query.substring(query.indexOf('=') + 1);
            System.err.println(nameSurname);
            String kandidat = nameSurname.split("\\+")[0].trim();
            System.err.println(kandidat);

            kandidat = URLDecoder.decode(kandidat, StandardCharsets.UTF_8);
            byte[] resultBytes = kandidat.getBytes(StandardCharsets.UTF_8);

            out.write(resultBytes);
            out.write('\r');
            out.write('\n');
            out.flush();

            this.connected = true;
        }
    }
}
