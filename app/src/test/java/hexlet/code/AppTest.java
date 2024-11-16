package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

public class AppTest {

    private static Javalin app;
    private static final MockWebServer MOCK_WEB_SERVER = new MockWebServer();

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        app.stop();
        MOCK_WEB_SERVER.shutdown();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.indexPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Сайты");
        });
    }

    @Test
    void testUrlPage() throws SQLException {
        var url = new Url("https://example.com");
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/99999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https%3A%2F%2Fwww.example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("url");
        });
    }

    @Test
    public void testCreateUrlCheck() throws Exception {
        String expectedBody = Files.readString(Path.of("src/test/resources/example.html"));
        MOCK_WEB_SERVER.enqueue(new MockResponse().setBody(expectedBody));
        MOCK_WEB_SERVER.start();
        HttpUrl mockWebUrl = MOCK_WEB_SERVER.url("/");

        var url = new Url(mockWebUrl.toString());
        UrlRepository.save(url);

        HttpResponse<String> mockResponse = Unirest.get(mockWebUrl.toString()).asString();

        var status = 200;
        var title = "Example test";
        var h1 = "h1 example";
        var description = "example site description";

        assertThat(mockResponse.getStatus()).isEqualTo(200);
        assertThat(mockResponse.getBody()).contains(title);
        assertThat(mockResponse.getBody()).contains(h1);
        assertThat(mockResponse.getBody()).contains(description);

        var urlCheck = new UrlCheck(url.getId(), status, title, h1, description);
        UrlCheckRepository.save(urlCheck);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
            var body = response.body().string();
            assertThat(body).contains(title);
            assertThat(body).contains(h1);
            assertThat(body).contains(description);
            assertThat(body).contains(description);
        });
    }
}
