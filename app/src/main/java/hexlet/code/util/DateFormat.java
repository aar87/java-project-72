package hexlet.code.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormat {
    public static final String DATE_FORMAT_VALUE = "yyyy-MM-dd HH:mm";

    public static String build(LocalDateTime date) {
        return DateTimeFormatter.ofPattern(DATE_FORMAT_VALUE).format(date);
    }
}
