package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;


public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final String DATABASE_SCHEMA = "schema.sql";
    private static final String APP_PORT_DEFAULT = "7070";
    private static final String DATABASE_URL = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";

    private static String readSchemaFile() throws IOException {
        var schemaStream = App.class.getClassLoader().getResourceAsStream(DATABASE_SCHEMA);
        assert schemaStream != null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(schemaStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        log.info("App main init");
        var javalin = getApp();
        javalin.start(Integer.parseInt(System.getenv().getOrDefault("PORT", APP_PORT_DEFAULT)));
        log.info("App is running");
    }

    public static Javalin getApp() throws IOException, SQLException {
        log.info("Setup database");
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.valueOf(System.getenv().getOrDefault("JDBC_DATABASE_URL", DATABASE_URL)));

        log.info("Init database structures");
        var dataSource = new HikariDataSource(hikariConfig);
        var sql = readSchemaFile();

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        log.info("Setup routes");
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World"));

        return app;
    }
}
