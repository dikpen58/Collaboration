package com.example.beautysalon.controllers;

import com.example.beautysalon.MainApp;
import com.example.beautysalon.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AuthController {
    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerConfirmPassword;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button loginTabButton;
    @FXML private Button registerTabButton;
    @FXML private VBox loginPane;
    @FXML private VBox registerPane;
    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        loginTabButton.setOnAction(e -> showLoginForm());
        registerTabButton.setOnAction(e -> showRegisterForm());
    }

    private void showLoginForm() {
        loginPane.setVisible(true);
        loginPane.setManaged(true);
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        loginTabButton.setStyle("-fx-background-color: #8B008B; -fx-text-fill: white; " +
                "-fx-background-radius: 5 0 0 5; -fx-border-radius: 5 0 0 5; " +
                "-fx-border-width: 1; -fx-border-color: #8B008B; " +
                "-fx-padding: 10 30; -fx-font-weight: bold;");
        registerTabButton.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: #666; " +
                "-fx-background-radius: 0 5 5 0; -fx-border-radius: 0 5 5 0; " +
                "-fx-border-width: 1; -fx-border-color: #8B008B; " +
                "-fx-padding: 10 30; -fx-font-weight: bold;");
    }

    private void showRegisterForm() {
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        registerPane.setVisible(true);
        registerPane.setManaged(true);
        loginTabButton.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: #666; " +
                "-fx-background-radius: 5 0 0 5; -fx-border-radius: 5 0 0 5; " +
                "-fx-border-width: 1; -fx-border-color: #8B008B; " +
                "-fx-padding: 10 30; -fx-font-weight: bold;");
        registerTabButton.setStyle("-fx-background-color: #8B008B; -fx-text-fill: white; " +
                "-fx-background-radius: 0 5 5 0; -fx-border-radius: 0 5 5 0; " +
                "-fx-border-width: 1; -fx-border-color: #8B008B; " +
                "-fx-padding: 10 30; -fx-font-weight: bold;");
    }

    @FXML
    private void handleLogin() {
        String username = loginUsername.getText().trim();
        String password = loginPassword.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Заполните все поля!");
            return;
        }
        if (userService.login(username, password)) {
            if (userService.getCurrentUser().isAdmin()) {
                MainApp.changeScene("admin_main.fxml", "Панель администратора - Studio Mariana");
            } else {
                MainApp.changeScene("user_main.fxml", "Studio Mariana - Главная");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Неверный логин или пароль!");
        }
    }

    @FXML
    private void handleRegister() {
        String username = registerUsername.getText().trim();
        String password = registerPassword.getText().trim();
        String confirmPassword = registerConfirmPassword.getText().trim();
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Заполните все поля!");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Пароли не совпадают!");
            return;
        }
        if (userService.register(username, password, false)) {
            if (userService.login(username, password)) {
                MainApp.changeScene("user_main.fxml", "Studio Mariana - Главная");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Пользователь с таким логином уже существует!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}