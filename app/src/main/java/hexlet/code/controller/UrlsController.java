package hexlet.code.controller;

import hexlet.code.UrlPage;
import hexlet.code.util.Flash;
import io.javalin.http.Context;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void list(Context ctx) {
        var page = new UrlPage();
        ctx.render("urls.jte", model("page", page));
    }

    public static void create(Context ctx) {
        var page = new UrlPage();

        String name = ctx.formParam("url");

        URL url;

        try {
            if (name == null || name.isEmpty()) {
                throw new MalformedURLException();
            }

            url = new URI(name).toURL();
        } catch (Exception e) {
            ctx.sessionAttribute("flash", new Flash("Некорректный URL", "danger"));
            ctx.redirect("/");
            return;
        }

        ctx.render("index.jte", model("page", page));
    }
}
