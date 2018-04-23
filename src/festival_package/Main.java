package festival_package;

import java.sql.*;


import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {





    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(new StackPane());

        Database.refresh_users();

        Parent root = FXMLLoader.load(getClass().getResource("login_window.fxml"));//change fxml back to login
        scene.setRoot(root);

        root.getStylesheets().add("festival_package/Styles.css");

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/festival_package/resources/new_logo_small_circle.png"));
        primaryStage.setTitle("Festival Connection");
        primaryStage.show();

    }





    public static void main(String[] args)
    {
        launch(args);
    }
}
