package hexlet.code.repository;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import hexlet.code.model.UrlCheck;


public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
            var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, urlCheck.getUrlId());
            preparedStatement.setInt(2, urlCheck.getStatusCode());
            preparedStatement.setString(3, urlCheck.getTitle());
            preparedStatement.setString(4, urlCheck.getH1());
            preparedStatement.setString(5, urlCheck.getDescription());
            var createdAt = LocalDateTime.now();
            preparedStatement.setTimestamp(6, Timestamp.valueOf(createdAt));

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
                urlCheck.setCreatedAt(createdAt);
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> getEntitiesForUrlId(Long id) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (var conn = dataSource.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();

            while (resultSet.next()) {
                var urlCheckId = resultSet.getLong("id");
                var urlId = resultSet.getLong("url_id");
                var status = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                var urlCheck = new UrlCheck(urlId, status, title, h1, description);
                urlCheck.setId(urlCheckId);
                urlCheck.setCreatedAt(createdAt);
                result.add(urlCheck);
            }
            return result;
        }
    }

    public static Map<Long, UrlCheck> getLatestUrlChecks() throws SQLException {
        var map = new HashMap<Long, UrlCheck>();
        var sql = "SELECT * FROM url_checks";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                long urlId = resultSet.getLong("url_id");
                long checkId = resultSet.getLong("id");
                int statusCode = resultSet.getInt("status_code");
                String h1 = resultSet.getString("h1");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                var urlCheck = new UrlCheck(urlId, statusCode, title, h1, description);
                urlCheck.setCreatedAt(createdAt);
                urlCheck.setId(checkId);
                map.put(urlId, urlCheck);
            }
            return map;
        }
    }
}
