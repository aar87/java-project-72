package hexlet.code.dto;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UrlListPage extends BasePage {
    private List<Url> urls;
    private Map<Long, UrlCheck> urlChecks;
}
