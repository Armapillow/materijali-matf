package Handler;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class QuizHandler extends URLStreamHandler {
    @Override
    protected int getDefaultPort() {
        return QuizConnection.DEFAULT_PORT;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new QuizConnection(u);
    }
}
