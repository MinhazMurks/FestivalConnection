package festival_package;

import java.sql.*;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {





    @Override
    public void start(Stage primaryStage) throws Exception{


        Database.refresh_users();

        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));

        root.getStylesheets().add("festival_package/Styles.css");

        primaryStage.setScene(new Scene(root,1280, 800));
        primaryStage.getIcons().add(new Image("file:src/festival_package/resources/Festival Logo2.png"));
        primaryStage.setTitle("Festival Connection");
        primaryStage.show();
    }





    public static void main(String[] args) {



        launch(args);
    }
}
