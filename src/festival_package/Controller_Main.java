package festival_package;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.soap.Text;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;


public class Controller_Main {

    private Stage primaryStage = new Stage();
    private AnchorPane rootPane;


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
        primaryStage.getIcons().add(new Image("file:src/festival_package/resources/Festival Logo2.png"));
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

    public void on_search_button(ActionEvent event)
    {
        System.out.println("Fuuuuck!");



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
