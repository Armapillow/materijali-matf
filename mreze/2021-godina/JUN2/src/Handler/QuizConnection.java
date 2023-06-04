package Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class QuizConnection extends URLConnection {

    public static final int DEFAULT_PORT = 12345;
    private Socket client = null;
    public QuizConnection(URL u) {
        super(u);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!this.connected)
            connect();

        return client.getInputStream();
    }

    @Override
    public void connect() throws IOException {
        if (!this.connected) {

            int port = url.getPort();
            if (port < 1 || port > 65535)
                port = DEFAULT_PORT;

            try {
                this.client = new Socket(url.getHost(), port);
            } catch (IOException ex) {
                System.err.println("Neuspela konekcija.");
                System.exit(1);
            }
            OutputStream out  = client.getOutputStream();

            String query = url.getQuery();
            System.err.println("Query: " + query);

            if (query == null || query.equalsIgnoreCase("")) {
                System.err.println("Nije uneta oblast.");
                System.exit(1);
            }

            String oblast = query.substring(query.indexOf('=') + 1).trim();
            System.err.println("Oblast: " + oblast);

            oblast = URLDecoder.decode(oblast, StandardCharsets.UTF_8);
            byte[] oblastBytes = oblast.getBytes(StandardCharsets.UTF_8);

            out.write(oblastBytes);
            out.write('\n');
            out.flush();


            this.connected = true;
        }
    }
}
