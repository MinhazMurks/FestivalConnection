package festival_package;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.SQLException;


public class Controller_Bookmarks_Friends
{

    @FXML
    ListView friends_listview;

    @FXML
    ListView bookmarks_listview;


    ListProperty<String> listProperty = new SimpleListProperty<>();
    ListProperty<String> listProperty2 = new SimpleListProperty<>();



    @FXML
    public void initialize()
    {

        listProperty.set(FXCollections.observableArrayList(Database.cur_user.Friend_Names));
        friends_listview.itemsProperty().bind(listProperty);

        listProperty2.set(FXCollections.observableArrayList(Database.cur_user.Bookmark_Names));
        bookmarks_listview.itemsProperty().bind(listProperty2);

    }

    public void on_delete_friend_button()
    {
        String friend_ID = Database.cur_user.Friends.get(friends_listview.getSelectionModel().getSelectedIndex()).userID;

        try {
            Database.remove_friend(friend_ID);
            listProperty.set(FXCollections.observableArrayList(Database.cur_user.Friend_Names));
            friends_listview.itemsProperty().bind(listProperty);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void on_delete_bookmark_button()
    {
        String festID = Database.cur_user.Bookmarks.get(friends_listview.getSelectionModel().getSelectedIndex()).festID;

        try {
            Database.remove_bookmark(festID);
            listProperty2.set(FXCollections.observableArrayList(Database.cur_user.Bookmark_Names));
            bookmarks_listview.itemsProperty().bind(listProperty2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
