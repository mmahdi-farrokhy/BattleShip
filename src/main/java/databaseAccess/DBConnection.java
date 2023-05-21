package databaseAccess;

import model.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class DBConnection {
    private static Connection connection;
    private static Statement statement;
    private static String host;
    private static String username;
    private static String password;

    private DBConnection() {
    }

    public static void openConnection() {
        initializeDatabase();
        try {
            connection = getConnection(host, username, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void initializeDatabase() {
        try (InputStream configFile = Files.newInputStream(Paths.get("db-config.properties"))) {
            final Properties properties = new Properties();
            properties.load(configFile);
            host = properties.get("host").toString();
            username = properties.get("user").toString();
            password = properties.get("pass").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int createUser(User user) throws SQLException {
        openConnection();
        String query = String.format("INSERT INTO users(userName, firstName, lastName, score, password) VALUES('%s','%s','%s',%d, '%s')",
                user.getUserName(), user.getFirstName(), user.getLatName(), user.getScore(), user.getPassword());
        statement.execute(query, Statement.RETURN_GENERATED_KEYS);

        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.next();
        int id = resultSet.getInt(1);
        closeConnection();
        return id;
    }

    public static void deleteUser(int userId) throws SQLException {
        openConnection();
        statement.execute(String.format("DELETE FROM users WHERE id = %d", userId));
        closeConnection();
    }

    public static ArrayList<User> getAllUsers() throws SQLException {
        openConnection();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(generateUserFromResultSet(resultSet));
        }

        closeConnection();
        return users;
    }

    public static void updateUser(User user) throws SQLException {
        openConnection();
        statement.execute(String.format("UPDATE users SET userName = '%s', firstName = '%s', lastName = '%s', score = %d WHERE id = %d",
                user.getUserName(), user.getFirstName(), user.getLatName(), user.getScore(), user.getId()));
        closeConnection();
    }

    private static User generateUserFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String userName = resultSet.getString(2);
        String firstName = resultSet.getString(3);
        String lastName = resultSet.getString(4);
        int score = resultSet.getInt(5);
        String password = resultSet.getString(6);
        return new User(id, userName, firstName, lastName, score, password);
    }
}
