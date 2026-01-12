package com.example.beautysalon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static Stage primaryStage;
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLoginScreen();
        stage.setTitle("Studio Mariana - Beauty Salon");
        stage.show();
    }

    public static void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/views/auth.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            scene.getStylesheets().add(MainApp.class.getResource("/styles/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(WINDOW_WIDTH);
            primaryStage.setMinHeight(WINDOW_HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/views/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
                scene.getStylesheets().add(MainApp.class.getResource("/styles/styles.css").toExternalForm());
            } else {
                scene.setRoot(root);
            }
            primaryStage.setTitle(title);
            primaryStage.setMinWidth(WINDOW_WIDTH);
            primaryStage.setMinHeight(WINDOW_HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}