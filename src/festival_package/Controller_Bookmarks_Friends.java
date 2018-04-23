package festival_package;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.sql.SQLException;


public class Controller_Bookmarks_Friends
{

    @FXML
    ListView friends_listview;

    @FXML
    TextField friend_search_field;

    @FXML
    ListView bookmarks_listview;

    @FXML
    Button friend_search_button;


    ListProperty<String> listProperty = new SimpleListProperty<>();
    ListProperty<String> listProperty2 = new SimpleListProperty<>();



    @FXML
    public void initialize()
    {
        friend_search_field.setOnKeyPressed(
                event ->
                {
                    if(event.getCode() == KeyCode.ENTER)
                    {
                        friend_search_button.fire();
                    }
                }
        );

        listProperty.set(FXCollections.observableArrayList(Database.viewed_friends_names));
        friends_listview.itemsProperty().bind(listProperty);

        listProperty2.set(FXCollections.observableArrayList(Database.cur_user.Bookmark_Names));
        bookmarks_listview.itemsProperty().bind(listProperty2);

    }

    public void on_delete_friend_button()
    {

        if(Database.cur_user.Friends.isEmpty() | friends_listview.getSelectionModel().getSelectedIndex() == -1)
        {
            System.out.println("No selection!");
            return;
        }

        String friend_ID = Database.cur_user.Friends.get(friends_listview.getSelectionModel().getSelectedIndex()).userID;

        try {
            Database.remove_friend(friend_ID);
            listProperty.set(FXCollections.observableArrayList(Database.viewed_friends_names));
            friends_listview.itemsProperty().bind(listProperty);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No selection!");
        }
    }

    public void on_delete_bookmark_button()
    {

        if(Database.cur_user.Bookmarks.isEmpty() | bookmarks_listview.getSelectionModel().getSelectedIndex() == -1)
        {
            System.out.println("No selection!");
            return;
        }

        String festID = Database.cur_user.Bookmarks.get(bookmarks_listview.getSelectionModel().getSelectedIndex()).festID;

        try {
            Database.remove_bookmark(festID);
            listProperty2.set(FXCollections.observableArrayList(Database.cur_user.Bookmark_Names));
            bookmarks_listview.itemsProperty().bind(listProperty2);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No selection!");
        }
    }

    public void search_friends()
    {

        String search_val = friend_search_field.getText();


        try {
            Database.search_friends(search_val);

            listProperty.set(FXCollections.observableArrayList(Database.viewed_friends_names));
            friends_listview.itemsProperty().bind(listProperty);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
