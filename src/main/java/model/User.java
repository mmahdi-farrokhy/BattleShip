package model;

import databaseAccess.DBConnection;

import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    private int id = -1;
    private String userName;
    private String firstName;
    private String latName;
    private int score;
    private String password;

    public User(int id, String userName, String firstName, String latName, int score, String password) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.latName = latName;
        this.score = score;
        this.password = password;
    }

    public User(String userName, String firstName, String latName, int score, String password) {
        this.userName = userName;
        this.firstName = firstName;
        this.latName = latName;
        this.score = score;
        this.password = password;
    }

    public static ArrayList<User> getAllUsers() {
        try {
            return DBConnection.getAllUsers();
        } catch (SQLException e) {
            return null;
        }
    }

    public void delete() {
        try {
            DBConnection.deleteUser(this.id);
            this.id = -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void save() {
        if (this.id == -1) {
            try {
                id = DBConnection.createUser(this);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                DBConnection.updateUser(this);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLatName() {
        return latName;
    }

    public void setLatName(String latName) {
        this.latName = latName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
