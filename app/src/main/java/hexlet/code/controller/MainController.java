package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;


public class MainController {
    public static void form(Context ctx) {
        var currentPage = new BasePage();
        currentPage.setFlash(ctx.consumeSessionAttribute("flash"));

        ctx.render("index.jte", model("page", currentPage));
    }
}
