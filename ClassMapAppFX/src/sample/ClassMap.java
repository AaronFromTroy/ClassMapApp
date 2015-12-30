package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.Button;
import java.awt.Label;
import java.awt.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassMap extends Application {


    public Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{

        Image img = new Image("sample/OrangeIcon.png");

        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(img);
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(5));
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("login-root");

        Image icon = new Image("sample/WhiteLogo.png");
        ImageView iv = new ImageView();
        iv.setImage(icon);
        iv.setFitWidth(120);
        iv.setFitHeight(120);
        iv.setPreserveRatio(true);

        Group root = new Group();
        HBox box = new HBox();
        box.getChildren().add(iv);
        root.getChildren().add(box);
        GridPane.setConstraints(root, 1, 0);

        javafx.scene.control.Label nameLabel = new javafx.scene.control.Label("Username");
        GridPane.setConstraints(nameLabel, 1, 1);

        javafx.scene.control.TextField nameInput = new javafx.scene.control.TextField();
        GridPane.setConstraints(nameInput, 2, 1);

        javafx.scene.control.Label passLabel = new javafx.scene.control.Label("Password");
        GridPane.setConstraints(passLabel, 1, 2);

        PasswordField passInput = new PasswordField();
        GridPane.setConstraints(passInput, 2, 2);

        javafx.scene.control.Button login = new javafx.scene.control.Button("Login");
        login.getStyleClass().add("login-button");
        GridPane.setConstraints(login, 2, 3);

        javafx.scene.control.Button signUp = new javafx.scene.control.Button("Sign Up");
        signUp.getStyleClass().add("login-button");
        GridPane.setConstraints(signUp, 2, 4);

        grid.getChildren().addAll(nameLabel, nameInput, passLabel, passInput, login, signUp, root);

        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (DataConnection.login(nameInput.getText(), passInput.getText()) == true) {
                    try {
                        new Login().start(primaryStage);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Alert.display("Error", "Incorrect username or password.");
                }
            }
        });

        passInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
            {
                if (DataConnection.login(nameInput.getText(), passInput.getText()) == true) {
                    try {
                        new Login().start(primaryStage);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Alert.display("Error", "Incorrect username or password.");
                }
            }
        });

        signUp.setOnAction(e -> {
            try {
                Register.register(primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        Scene loginScene = new Scene(grid, 350, 270);
        loginScene.getStylesheets().add("Cobra.css");
        primaryStage.setResizable(false);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }




    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
