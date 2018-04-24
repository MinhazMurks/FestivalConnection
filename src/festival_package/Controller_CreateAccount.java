package festival_package;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;

public class Controller_CreateAccount {

    @FXML
    AnchorPane create_account_anchor;

    @FXML
    TextField username_field;

    @FXML
    Text username_error_text;

    @FXML
    Text username_invalid_text;

    @FXML
    TextField password_field;

    @FXML
    Text retype_password_error_text;

    @FXML
    TextField retype_password_field;

    @FXML
    DatePicker dob_field;

    @FXML
    ChoiceBox<String> state_dropdown;

    @FXML
    TextField city_field;

    @FXML
    TextField address_field;

    @FXML
    TextField zip_field;

    @FXML
    Button create_account_button;

    @FXML
    Text sql_error_text;

    /**
     * Sets error texts to invisible.
     */
    @FXML
    public void initialize(){
        username_error_text.setVisible(false); retype_password_error_text.setVisible(false); sql_error_text.setVisible(false);

        zip_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    zip_field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * Does basic checks to see if all fields are properly filled out before inserting data into
     * User and Location tables. Checks availability of username with Database.usernameAvailable(String)
     * Closes window upon successful creation.
     * @param event
     * @throws SQLException
     */
    public void on_create_account_button(ActionEvent event){
        //Reset error messages
        username_error_text.setVisible(false); retype_password_error_text.setVisible(false); sql_error_text.setVisible(false);
        //Get the create account page anchor so we can close it later
        Stage stage = (Stage) create_account_anchor.getScene().getWindow();

        String emptyString = "";
        String username = username_field.getText();
        String password = password_field.getText();
        String retype = retype_password_field.getText();
        LocalDate dob = dob_field.getValue();
        String state = emptyString;

        if(!username.matches("[a-z0-9_-]++"))
        {
            username_invalid_text.setVisible(true);
            return;
        }

        username_invalid_text.setVisible(false);

        if (state_dropdown.getValue() != null) {
            state = state_dropdown.getValue();
        }
        String city = city_field.getText();
        String address = address_field.getText();
        //Checks to be sure the zip code is a 5 digit int
        int zip = -1;
        if (!zip_field.getText().equals(emptyString)) {
            if ((zip_field.getText().length() == 5)){
                zip = Integer.parseInt(zip_field.getText());
            }
        }

        if ((username.equals(emptyString)) ||
                (password.equals(emptyString)) ||
                (retype.equals(emptyString)) ||
                (dob == null) ||
                (state.equals(emptyString)) ||
                (city.equals(emptyString)) ||
                (address.equals(emptyString)) ||
                (zip < 0)) {
            return;
        }

        if (!password.equals(retype)){
            retype_password_error_text.setVisible(true);
            return;
        }

        try {
            if (!Database.usernameAvailable(username)) {
                username_error_text.setVisible(true);
                return;
            }
            System.out.println("Creating Account...");
            System.out.print(username + "\n" + password + "\n" + dob.toString() + "\n" + address + "\n" + city + "\n" + state + "\n" + zip + "\n");
            Database.insertNewUser(username, password, dob.toString(), state, city, address, zip);
            Database.refresh_users();
            stage.close();
        }
        catch (SQLException e){
            sql_error_text.setVisible(true);
            e.printStackTrace();
            return;
        }
        catch (ParseException e){
            e.printStackTrace();
            return;
        }
    }

}
