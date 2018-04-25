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

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;


public class Controller_Main {




    private Stage primaryStage = new Stage();
    private AnchorPane rootPane;

    //the guid of the logged in User. Used for many queries


    public static boolean is_edit = false;
    public static Festival selected_fest = null;

    @FXML
    public ListView search_listview;

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
    Button add_bookmark_button;

    @FXML
    Button add_festival_button;

    @FXML
    Button edit_festival_button;

    @FXML
    Button advanced_button;

    public static ListProperty<String> listProperty_main = new SimpleListProperty<>();



    public void changeScene(String fxml) throws IOException
    {
        Parent pane = FXMLLoader.load(
                getClass().getResource(fxml));

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("new_logo_small_circle.png")));
        primaryStage.setTitle(Database.fix_title(fxml));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    @FXML
    public void initialize()
    {
        add_festival_button.setVisible(false);
        if (Database.cur_user.is_company | Database.is_admin()){
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
        advanced_button.setDisable(true);
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

        listProperty_main.set(FXCollections.observableArrayList(Database.viewed_list));
        search_listview.itemsProperty().bind(listProperty_main);


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
                        search_listview.getSelectionModel().clearSelection();
                        advanced_button.setDisable(true);
                        edit_festival_button.setVisible(false);
                    }
                    else if(search_dropdown.getSelectionModel().getSelectedItem().equals("Festival"))
                    {
                        search_field.setText("");
                        Database.set_viewed_list(Database.Festivals);
                        search_listview.getSelectionModel().clearSelection();
                        //description_pane.setText("Click an entry to see more information");
                        advanced_button.setDisable(false);
                    }


                    listProperty_main.set(FXCollections.observableArrayList(Database.viewed_list));
                    search_listview.itemsProperty().bind(listProperty_main);
                }

        );

        search_listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {

                if(search_listview.getSelectionModel().getSelectedIndex() == -1)
                {
                    description_pane.setText("Click an entry to see more information");
                    add_bookmark_button.setVisible(false);
                    add_friend_button.setVisible(false);

                    edit_festival_button.setVisible(false);
                }

                if(search_dropdown.getSelectionModel().getSelectedItem().equals("User"))
                {
                    add_bookmark_button.setVisible(false);
                    edit_festival_button.setVisible(false);
                    try {


                        User selected_user = Database.user_from_userID(Database.viewed_list_id.get(search_listview.getSelectionModel().getSelectedIndex()));

                        System.out.println("Selected user: " + selected_user.user_name + " userID: " + selected_user.userID);
                        System.out.println("Current Logged in user: " + Database.cur_user.user_name + " userID: " + Database.cur_user.userID);

                        if (!selected_user.equals(Database.cur_user) && !Database.cur_user.Friends.contains(selected_user)) {
                            add_friend_button.setVisible(true);
                        } else {
                            add_friend_button.setVisible(false);
                        }

                        try {
                            description_pane.setText(Database.user_from_userID(Database.viewed_list_id.get(search_listview.getSelectionModel().getSelectedIndex())).description());

                        } catch (ArrayIndexOutOfBoundsException e) {
                            description_pane.setText("Click an entry to see more information");
                        }
                    }catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("No selection!");
                        add_friend_button.setVisible(false);
                    }

                }
                else
                {



                    add_friend_button.setVisible(false);
                    if(Database.is_admin() && search_listview.getSelectionModel().getSelectedIndex() != -1)
                    {
                        edit_festival_button.setVisible(true);
                    }
                    try{

                        selected_fest = Database.fest_from_festID(Database.viewed_list_id.get(search_listview.getSelectionModel().getSelectedIndex()));

                    if(Database.cur_user.Bookmarks.contains(Database.Festivals.get(search_listview.getSelectionModel().getSelectedIndex())))
                    {
                        add_bookmark_button.setVisible(false);
                    }
                    else
                    {
                        add_bookmark_button.setVisible(true);
                    }

                        description_pane.setText(Database.fest_from_festID(Database.viewed_list_id.get(search_listview.getSelectionModel().getSelectedIndex())).description());
                    }catch(ArrayIndexOutOfBoundsException e)
                    {
                        System.out.println("No selection!");
                    }
                }

            }
        });

    }
    public void on_search_button(ActionEvent event) throws SQLException, ParseException {
        //System.out.println("Fuuuuck!");

        //System.out.println("dropdown val: " + search_dropdown.getValue());

        if(!search_field.getText().isEmpty())
        {
            Database.name_search(search_field.getText(), search_dropdown.getValue());
        }
        else{
            Database.reset_view_list(search_dropdown.getValue());
        }

        Database.refresh_lists();


        listProperty_main.set(FXCollections.observableArrayList(Database.viewed_list));
        //search_listview.itemsProperty().bind(listProperty_main);

    }

    public void on_friend_button(ActionEvent event) throws IOException
    {
        changeScene("Bookmarks_and_Friends.fxml");
        System.out.println("Friend adding");

        search_listview.getSelectionModel().clearSelection();
    }
    public void on_favorite_button(ActionEvent event) throws IOException
    {
        changeScene("Bookmarks_and_Friends.fxml");
        System.out.println("Favorite adding");

        search_listview.getSelectionModel().clearSelection();
    }
    public void on_advanced_button(ActionEvent event) throws IOException {
        changeScene("AdvancedSearch.fxml");
        System.out.println("advanced search");

        search_listview.getSelectionModel().clearSelection();
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

    public void add_bookmark_button(ActionEvent event)
    {
        Festival selected_festival = Database.fest_from_festID(Database.viewed_list_id.get(search_listview.getSelectionModel().getSelectedIndex()));

        String table = "Bookmarks(userID, festID)";

        ArrayList<String> values = new ArrayList<>();
        values.add(Database.cur_user.userID);
        values.add(selected_festival.festID);

        try {
            Database.insert_to_table(table, values);
            Database.refresh_bookmarks();
            add_bookmark_button.setVisible(false);
        } catch (SQLException e) {
            System.out.println(Database.cur_user.Bookmarks.toString());
            System.out.println("Already Bookmarked!");
        }

    }

    public void on_add_festival_button(ActionEvent event) throws IOException{
        changeScene("add_festival_window.fxml");
        System.out.println("adding festival");
    }

    public void on_edit_festival(ActionEvent event)
    {
        is_edit = true;
        add_festival_button.fire();
    }

}
