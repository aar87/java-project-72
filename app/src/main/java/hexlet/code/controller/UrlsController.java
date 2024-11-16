package hexlet.code.controller;

import hexlet.code.dto.UrlListPage;
import hexlet.code.dto.UrlPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.Flash;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlNameBuilder;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void list(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();
        Map<Long, UrlCheck> urlChecks = UrlCheckRepository.getLatestUrlChecks();
        var page = new UrlListPage(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        String name = ctx.formParam("url");
        String hostname;

        try {
            hostname = UrlNameBuilder.create(name);
        } catch (Exception e) {
            ctx.sessionAttribute("flash", new Flash("Некорректный URL", "danger"));
            ctx.redirect(NamedRoutes.indexPath());
            return;
        }

        if (UrlRepository.isNameAlreadyExists(hostname)) {
            ctx.sessionAttribute("flash", new Flash("Страница уже существует", "info"));
            ctx.redirect(NamedRoutes.indexPath());
            return;
        }

        UrlRepository.save(new Url(hostname));
        ctx.sessionAttribute("flash", new Flash("Страница успешно добавлена", "success"));
        ctx.redirect(NamedRoutes.urlsPath());
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));
        var urlChecks = UrlCheckRepository.getEntitiesForUrlId(id);
        var page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/show.jte", model("page", page));
    }
}
