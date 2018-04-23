package festival_package;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;

public class Controller_Login {

    //Used for getting the login screen's window in order to close it at successful login
    @FXML
    AnchorPane login_anchor;

    @FXML
    TextField username_field;

    @FXML
    PasswordField password_field;

    @FXML
    Button login_button;

    @FXML
    Button create_account_button;

    @FXML
    Text login_error_text;

    /**
     * Sets error text to invisible.
     */
    @FXML
    public void initialize(){

        // username_field.setFont(Font.loadFont("resources/Raleway-Bold.ttf", 200));



        login_error_text.setVisible(false);
        username_field.setOnKeyPressed(
                event ->
                {
                    if(event.getCode() == KeyCode.ENTER)
                    {
                        login_button.fire();
                    }
                }
        );

        password_field.setOnKeyPressed(
                event ->
                {
                    if(event.getCode() == KeyCode.ENTER)
                    {
                        login_button.fire();
                    }
                }
        );

        this.auto_log_in();
    }

    private void auto_log_in()
    {
        username_field.setText("MinhazMurks");
        password_field.setText("pass");
    }


    /**
     * Uses authenticate(String, String) helper method in Database.java to query database.
     * Sets login_error_text to visible if empty textfield or the given username is not in the ResultSet of the query.
     * TODO: Add password authentication
     * If username is present in database, stores guid and launches main window, passing username and guid to
     * main window using launchMainWindow(String, String)
     * @param event
     * @throws SQLException
     */
    public void on_login_button(ActionEvent event) {

        Stage stage =(Stage) login_anchor.getScene().getWindow();
        String username = username_field.getText();
        String password = password_field.getText();

        if (username.equals("")){
            login_error_text.setVisible(true);
        }
        else{

            boolean authenticated = false;

                try {
                    authenticated = Database.authenticate(username, password);
                    System.out.println("Authenticated? :" + authenticated);

                } catch (SQLException | ArrayIndexOutOfBoundsException e) {
                    login_error_text.setVisible(true);
                }



                if(authenticated)
                {
                    try {
                        launchMainWindow();
                        stage.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }



        }

        System.out.println("[" + username + "] Logging in...");

    }

    /**
     * Launches the main window for the program.
     * Creates a Controller_Main object so the guid can be stored in the userGuid field in Controller_Main.java.
     * Sets the title of the main window to display the user's username.

     *                  USING Controller_Main.initData(String) METHOD
     * @throws IOException
     */
    public void launchMainWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_window.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        stage.getIcons().add(new Image("file:src/festival_package/resources/Festival_Logo2.png"));
        stage.setTitle("Festival Connection - Welcome " + Database.cur_user.user_name);

        stage.show();
    }




    public void on_create_account_button(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("create_account_window.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        stage.getIcons().add(new Image("file:src/festival_package/resources/Festival_Logo2.png"));
        stage.setTitle("Create Account");

        Controller_CreateAccount controller = loader.<Controller_CreateAccount>getController();

        stage.show();
    }
}
