package Handlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class FarmHandler extends URLStreamHandler {

    @Override
    protected int getDefaultPort() {
        return FarmConnection.DEFAULT_PORT;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new FarmConnection(u);
    }
}
