package hexlet.code.dto;
import hexlet.code.model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UrlListPage extends BasePage {
    private List<Url> urls;
}
