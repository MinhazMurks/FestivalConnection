package festival_package;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;


public class Controller_Main {

    private Stage primaryStage = new Stage();
    private AnchorPane rootPane;

    //the guid of the logged in User. Used for many queries


    @FXML
    ListView search_listview;

    @FXML
    ComboBox<String> search_dropdown = new ComboBox<>();

    @FXML
    Button search_button;

    @FXML
    Label description_pane;

    @FXML
    TextField search_field;

    @FXML
    Button add_friend_button;

    @FXML
    Button add_festival_button;

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
        add_festival_button.setVisible(false);
        if (Database.cur_user.is_company){
            add_festival_button.setVisible(true);
        }

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Festival");
        choices.add("User");

        try {
            Database.refresh_lists();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        search_field.setOnKeyPressed(
                event ->
                {
                    if(event.getCode() == KeyCode.ENTER)
                    {
                        search_button.fire();
                    }
                }
        );

        search_dropdown.getItems().addAll(choices);
        search_dropdown.setValue("User");
        search_dropdown.setButtonCell(new ListCell<String>()
        {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setAlignment(Pos.CENTER_RIGHT);
                }
            }

        });


        search_dropdown.setValue("User");

        try {
            Database.set_viewed_list(Database.Users);
            Database.refresh_users();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        listProperty.set(FXCollections.observableArrayList(Database.viewed_list));
        search_listview.itemsProperty().bind(listProperty);


        search_dropdown.setOnAction(
                event ->
                {


                    try {
                        Database.refresh_lists();
                    } catch (SQLException e) {
                        System.out.println("Empty");
                    } catch (ParseException e)
                    {
                        System.out.println("Parse failed");
                    }

                    if(search_dropdown.getSelectionModel().getSelectedItem().equals("User"))
                    {
                        search_field.setText("");
                        Database.set_viewed_list(Database.Users);
                    }
                    else if(search_dropdown.getSelectionModel().getSelectedItem().equals("Festival"))
                    {
                        search_field.setText("");
                        Database.set_viewed_list(Database.Festivals);
                    }


                    listProperty.set(FXCollections.observableArrayList(Database.viewed_list));
                    search_listview.itemsProperty().bind(listProperty);
                }

        );

        search_listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                if(search_dropdown.getSelectionModel().getSelectedItem().equals("User"))
                {
                    User selected_user = Database.user_from_userID(Database.viewed_list_id.get(search_listview.getSelectionModel().getSelectedIndex()));

                    System.out.println("Selected user: " + selected_user.user_name + " userID: " + selected_user.userID);
                    System.out.println("Current Logged in user: " + Database.cur_user.user_name + " userID: " + Database.cur_user.userID);

                    if(!selected_user.equals(Database.cur_user) && !Database.cur_user.Friends.contains(selected_user))
                    {
                        add_friend_button.setVisible(true);
                    }
                    else
                    {
                        add_friend_button.setVisible(false);
                    }

                    try
                    {
                        description_pane.setText(Database.user_from_userID(Database.viewed_list_id.get(search_listview.getSelectionModel().getSelectedIndex())).description());

                    }catch(ArrayIndexOutOfBoundsException e)
                    {
                        description_pane.setText("Click an entry to see more information");
                    }

                }
                else
                {
                    add_friend_button.setVisible(false);
                    try
                    {
                        description_pane.setText(Database.fest_from_festID(Database.viewed_list_id.get(search_listview.getSelectionModel().getSelectedIndex())).description());
                    }catch(ArrayIndexOutOfBoundsException e)
                    {
                        description_pane.setText("Click an entry to see more information");
                    }
                }

            }


        });

    }

    public void on_search_button(ActionEvent event) throws SQLException, ParseException {
        System.out.println("Fuuuuck!");

        System.out.println("dropdown val: " + search_dropdown.getValue());

        if(!search_field.getText().isEmpty())
        {
            Database.name_search(search_field.getText(), search_dropdown.getValue());
        }
        else{
            Database.reset_view_list(search_dropdown.getValue());
        }

        Database.refresh_lists();


        listProperty.set(FXCollections.observableArrayList(Database.viewed_list));
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

    public void add_friend_button(ActionEvent event) {

        User selected_user = Database.user_from_userID(Database.viewed_list_id.get(search_listview.getSelectionModel().getSelectedIndex()));

        String table = "Friends(user1, user2)";

        ArrayList<String> values = new ArrayList<>();
        values.add(Database.cur_user.userID);
        values.add(selected_user.userID);

        try {
            Database.insert_to_table(table, values);
            Database.refresh_friends();
            add_friend_button.setVisible(false);

        } catch (SQLException e) {

            System.out.println(Database.cur_user.Friends.toString());
            System.out.println("Already Friends!");
        }
    }

    public void on_add_festival_button(ActionEvent event) throws IOException{
        changeScene("add_festival_window.fxml");
        System.out.println("adding festival");
    }
}
