package festival_package;

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
import java.time.LocalDate;

public class Controller_CreateAccount {

    @FXML
    TextField username_field;

    @FXML
    Text username_error_text;

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
    public void initialize(){username_error_text.setVisible(false); retype_password_error_text.setVisible(false); sql_error_text.setVisible(false);}

    /**
     * Does basic checks to see if all fields are properly filled out before inserting data into
     * User and Location tables. Checks availability of username with Database.usernameAvailable(String)
     * @param event
     * @throws SQLException
     */
    public void on_create_account_button(ActionEvent event) throws SQLException{
        //Reset error messages
        username_error_text.setVisible(false); retype_password_error_text.setVisible(false); sql_error_text.setVisible(false);

        String emptyString = "";
        String username = username_field.getText();
        String password = password_field.getText();
        String retype = retype_password_field.getText();
        LocalDate dob = dob_field.getValue();
        String state = emptyString;
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

        if (!Database.usernameAvailable(username)){
            username_error_text.setVisible(true);
            return;
        }

        System.out.println("Creating Account...");
        System.out.print(username + "\n" + password + "\n" + dob.toString() + "\n" + address + "\n" + city + "\n" + state + "\n" + zip + "\n");

    }

}
