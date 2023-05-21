package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegisterPageController implements Initializable {
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLbl;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField lastnameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button registerBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLbl.setText("");
        cancelBtn.setOnAction(e -> closeRegisterStage());
        registerBtn.setOnAction(e -> createUser());
    }

    public void closeRegisterStage() {
        ((Stage) cancelBtn.getScene().getWindow()).close();
        LoginPageController.registerStage = null;
    }

    private void createUser() {
        if (!allFieldsAreFilled()) {
            errorLbl.setText("Please fill in all fields");
            return;
        }

        if (validUsername() && passwordConfirmed()) {
            User user = new User(
                    usernameField.getText(),
                    firstnameField.getText(),
                    lastnameField.getText(),
                    0,
                    passwordField.getText());
            user.save();
            clearPage();
            closeRegisterStage();
        }

    }

    private boolean allFieldsAreFilled() {
        return !usernameField.getText().isEmpty() &&
                !firstnameField.getText().isEmpty() &&
                !lastnameField.getText().isEmpty() &&
                !passwordField.getText().isEmpty() &&
                !confirmPasswordField.getText().isEmpty();
    }

    private boolean validUsername() {
        ArrayList<User> allUsers = User.getAllUsers();
        for (User user : allUsers) {
            if (user.getUserName().equals(usernameField.getText().trim())) {
                errorLbl.setText("This username is already registered");
                return false;
            }
        }

        return true;
    }

    private boolean passwordConfirmed() {
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errorLbl.setText("Passwords do not match");
            return false;
        }

        return true;
    }

    private void clearPage() {
        usernameField.setText("");
        firstnameField.setText("");
        lastnameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        errorLbl.setText("");
    }
}
