
package com.example.application.Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class DatabaseService {
    private Connect_to_DataBase dbConnection;

    public DatabaseService() {
        try {
            dbConnection = Connect_to_DataBase.getInstance();
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    public List<Post> getAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String content = rs.getString("content");
                String heading = rs.getString("heading");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                posts.add(new Post(userId, content, heading, createdAt));
            }
            return posts;
        } catch (SQLException e) {
            // Обработка исключения
            throw e;
        }
    }

    public void savePost(Post post, List<Media> mediaList) throws SQLException {
        String sql = "INSERT INTO posts (user_id, content, heading) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, post.getUserId());
            stmt.setString(2, post.getContent());
            stmt.setString(3, post.getHeading());
            stmt.executeUpdate();

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int postId = generatedKeys.getInt(1);
                if (mediaList != null && !mediaList.isEmpty()) {
                    saveMedia(postId, mediaList);
                }
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    private void saveMedia(int postId, List<Media> mediaList) throws SQLException {
        String sql = "INSERT INTO media (post_id, media_type, media_data, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Media media : mediaList) {
                stmt.setInt(1, postId);
                stmt.setString(2, media.getMediaType());
                stmt.setString(3, media.getMediaData());
                stmt.setTimestamp(4, Timestamp.valueOf(media.getCreatedAt()));
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public List<Media> getMediaByPostId(int postId) throws SQLException {
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE post_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String mediaType = rs.getString("media_type");
                    String mediaData = rs.getString("media_data");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                    mediaList.add(new Media(id, postId, mediaType, mediaData, createdAt));
                }
            }
        }
        return mediaList;
    }

    public static void main(String[] args) throws SQLException {
        DatabaseService db = new DatabaseService();
        Post pt = new Post(1, "Привет2!", "Олух");
        db.savePost(pt, null);
        System.out.println(db.getAllPosts().toString());
        pt = new Post(1, "Привет23!", "Дима пи...");
        db.savePost(pt, null);
        System.out.println(db.getAllPosts().toString());
    }

}
