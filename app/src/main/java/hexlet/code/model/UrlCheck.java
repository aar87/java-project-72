package hexlet.code.model;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
public class UrlCheck {
    private long id;
    private Long urlId;
    private int statusCode;
    private String title;
    private String h1;
    private String description;
    private LocalDateTime createdAt;

    public UrlCheck(Long urlId, int statusCode, String title, String h1, String description) {
        this.urlId = urlId;
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
    }

    public UrlCheck(int statusCode) {
        new UrlCheck(null, statusCode, null, null, null);
    }
}
