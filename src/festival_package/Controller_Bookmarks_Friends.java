package festival_package;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


public class Controller_Bookmarks_Friends
{

    @FXML
    ListView friends_listview;

    @FXML
    ListView bookmarks_listview;


    ListProperty<String> listProperty = new SimpleListProperty<>();



    @FXML
    public void initialize()
    {

        listProperty.set(FXCollections.observableArrayList(Database.cur_user.Friend_Names));
        friends_listview.itemsProperty().bind(listProperty);

     friends_listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
     {
         @Override
         public void changed(ObservableValue observable, Object oldValue, Object newValue) {

         }
     });
    }
}
