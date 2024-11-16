package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.MainController;
import hexlet.code.controller.UrlCheckController;
import hexlet.code.controller.UrlsController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;


public class App {
    private static final Logger APP_LOG = LoggerFactory.getLogger(App.class);
    private static final String DATABASE_SCHEMA = "schema.sql";
    private static final String APP_PORT_DEFAULT = "7070";
    private static final String DATABASE_URL = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static String readSchemaFile() throws IOException {
        var schemaStream = App.class.getClassLoader().getResourceAsStream(DATABASE_SCHEMA);
        assert schemaStream != null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(schemaStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        APP_LOG.info("App main init");
        var javalin = getApp();
        javalin.start(Integer.parseInt(System.getenv().getOrDefault("PORT", APP_PORT_DEFAULT)));
        APP_LOG.info("App is running");
    }

    public static Javalin getApp() throws IOException, SQLException {
        APP_LOG.info("Setup database");
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.valueOf(System.getenv().getOrDefault("JDBC_DATABASE_URL", DATABASE_URL)));

        APP_LOG.info("Init database structures");
        var dataSource = new HikariDataSource(hikariConfig);
        var sql = readSchemaFile();

        APP_LOG.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        APP_LOG.info("Setup Javalin application");
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        APP_LOG.info("Setup routes");
        app.get(NamedRoutes.indexPath(), MainController::form);
        app.get(NamedRoutes.urlsPath(), UrlsController::list);
        app.post(NamedRoutes.urlsPath(), UrlsController::create);
        app.get(NamedRoutes.urlPath("{id}"), UrlsController::show);
        app.post(NamedRoutes.urlCheckPath("{id}"), UrlCheckController::create);

        return app;
    }
}
