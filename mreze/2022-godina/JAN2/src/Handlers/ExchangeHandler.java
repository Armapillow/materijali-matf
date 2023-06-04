package Handlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class ExchangeHandler extends URLStreamHandler {

    @Override
    protected int getDefaultPort() {
        return ExchangeURLConnection.PORT;
    }


    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new ExchangeURLConnection(u);
    }
}
