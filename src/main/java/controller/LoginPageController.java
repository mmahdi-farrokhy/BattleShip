package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    @FXML
    private Button enterBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLbl;

    static Stage registerStage = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLbl.setText("");

        registerBtn.setOnAction(e -> {
            try {
                openRegisterPage();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        exitBtn.setOnAction(e -> closeApplication());
        enterBtn.setOnAction(e -> starGame());
    }

    private void openRegisterPage() throws IOException {
        if (registerStage == null) {
            VBox root = FXMLLoader.load(this.getClass().getResource("../RegisterPage.fxml"));
            registerStage = new Stage();
            registerStage.setTitle("Register User");
            registerStage.setScene(new Scene(root));
            registerStage.show();
        }
    }

    private void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

    private void starGame() {
        if (allFieldsAreFilled()) {
            User user = validUsername(usernameField.getText().trim());
            if (user != null) {
                if (validPassword(user, passwordField.getText().trim())) {
                    try {
                        loadMainPage(user);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    errorLbl.setText("Wrong password!");
                }
            } else {
                errorLbl.setText("Username not found!");
            }
        } else {
            errorLbl.setText("Please fill in all fields.");
        }


    }

    private boolean allFieldsAreFilled() {
        return !usernameField.getText().isEmpty() &&
                !passwordField.getText().isEmpty();
    }

    private User validUsername(String username) {
        ArrayList<User> allUsers = User.getAllUsers();
        for (User user : allUsers)
            if (user.getUserName().equals(username))
                return user;

        return null;
    }

    private boolean validPassword(User user, String password) {
        return password.equals(user.getPassword());
    }

    private void loadMainPage(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../MainPage.fxml"));
        loader.load();
        MainPageController controller = loader.getController();
        controller.initPage(user);
        Stage stage = (Stage) registerBtn.getScene().getWindow();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
    }
}
