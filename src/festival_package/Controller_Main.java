package festival_package;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;


public class Controller_Main {

    private Stage primaryStage = new Stage();
    private AnchorPane rootPane;

    //the guid of the logged in User. Used for many queries


    @FXML
    ListView search_listview;

    @FXML
    Button search_button;

    @FXML
    Label description_pane;

    ListProperty<String> listProperty = new SimpleListProperty<>();



    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(
                getClass().getResource(fxml));

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/festival_package/resources/Festival_Logo2.png"));
        primaryStage.setTitle(Database.fix_title(fxml));
        primaryStage.show();

    }

    @FXML
    public void initialize()
    {
        listProperty.set(FXCollections.observableArrayList(Database.User_Names));

        search_listview.itemsProperty().bind(listProperty);

        search_listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                description_pane.setText((Database.Users.get(Database.User_Names.indexOf(search_listview.getSelectionModel().getSelectedItem())).description()));
            }
        });

    }

    public void on_search_button(ActionEvent event) throws SQLException, ParseException {
        System.out.println("Fuuuuck!");

        Database.refresh_users();

        listProperty.set(FXCollections.observableArrayList(Database.User_Names));

        search_listview.itemsProperty().bind(listProperty);
    }


    public void on_friend_button(ActionEvent event) throws IOException
    {
        changeScene("Bookmarks_and_Friends.fxml");
        System.out.println("Friend adding");
    }


    public void on_favorite_button(ActionEvent event) throws IOException
    {
        changeScene("Bookmarks_and_Friends.fxml");
        System.out.println("Favorite adding");
    }

    public void on_advanced_button(ActionEvent event) throws IOException {
        changeScene("AdvancedSearch.fxml");
        System.out.println("advanced search");
    }

}
