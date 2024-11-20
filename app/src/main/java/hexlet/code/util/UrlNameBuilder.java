package hexlet.code.util;

import org.apache.commons.codec.binary.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Locale;

enum Protocol {
    HTTP, HTTPS, FTP
}

public class UrlNameBuilder {
    public static boolean isValidProtocol(String protocol) {
        return Arrays.stream(Protocol.values())
                .anyMatch(p -> StringUtils.equals(p.toString(), protocol.toUpperCase(Locale.ENGLISH)));
    }

    public static String create(String name) {
        try {
            URI uri = new URI(name);
            String protocol = uri.getScheme().toLowerCase(Locale.ENGLISH);

            if (!isValidProtocol(protocol)) {
                throw new IllegalArgumentException("Invalid protocol: " + protocol);
            }

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
