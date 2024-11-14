package hexlet.code.dto;

import hexlet.code.util.Flash;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasePage {
    private Flash flash;
}
