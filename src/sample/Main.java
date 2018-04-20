package sample;

import java.sql.*;
import java.util.ArrayList;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {





    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));

        primaryStage.setScene(new Scene(root,1280, 800));
        primaryStage.getIcons().add(new Image("file:resources/test-image.png"));
        primaryStage.setTitle("Festival Connection");
        primaryStage.show();
    }





    public static void main(String[] args) {

        try(
                Connection connection = DriverManager.getConnection
                        (
                                "jdbc:mysql://festivalproject.mysql.database.azure.com/festivals_project",
                                "festival_admin@festivalproject",
                                "muK43her"
                        );

                Statement statement = connection.createStatement())
        {

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


        launch(args);
    }
}
