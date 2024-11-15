package hexlet.code.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class UrlNameBuilder {
    public static String create(String name) {
        try {
            URI uri = new URI(name);
            String protocol = uri.getScheme().toLowerCase(Locale.ENGLISH);
            String host = uri.getHost().toLowerCase(Locale.ENGLISH);
            int port = uri.getPort();
            if (port == -1) {
                return protocol + "://" + host;
            }
            return protocol + "://" + host + ":" + port;
        } catch (URISyntaxException e) {
            throw new RuntimeException("URISyntaxException: " + e.getMessage());
        }
    }
}
