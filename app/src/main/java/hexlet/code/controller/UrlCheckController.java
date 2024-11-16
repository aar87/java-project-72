package hexlet.code.controller;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.Flash;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Optional;


public class UrlCheckController {
    public static void create(Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();

        try {
            Optional<Url> url = UrlRepository.find(id);

            if (url.isEmpty()) {
                ctx.sessionAttribute("flash", new Flash("Не найден сайт для проверки", "danger"));
                ctx.redirect(NamedRoutes.indexPath());
                return;
            }

            Url foundUrl = url.get();
            HttpResponse<String> response = Unirest.get(foundUrl.getName()).asString();
            Document node = Jsoup.parse(response.getBody());

            int statusCode = response.getStatus();
            String title = node.title();
            String h1 = node.selectFirst("h1") != null
                    ? Objects.requireNonNull(node.selectFirst("h1")).text()
                    : "";

            String description = "";
            Element descriptionNode = node.selectFirst("meta[name=description]");

            if (descriptionNode != null) {
                if (descriptionNode.hasAttr("content")) {
                    description = descriptionNode.attr("content");
                }
            }

            UrlCheck urlCheck = new UrlCheck(id, statusCode, title, h1, description);
            UrlCheckRepository.save(urlCheck);
            ctx.sessionAttribute("flash", new Flash("Страница успешно проверена", "success"));
            ctx.redirect(NamedRoutes.urlPath(id));
        } catch (Exception e) {
            ctx.sessionAttribute("flash", new Flash("Некорректный адрес", "danger"));
            ctx.redirect(NamedRoutes.urlPath(id));
        }
    }
}
