package Handler;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class EvoteHandler extends URLStreamHandler {
    @Override
    protected int getDefaultPort() {
        return EvoteConnection.PORT;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new EvoteConnection(u);
    }
}
