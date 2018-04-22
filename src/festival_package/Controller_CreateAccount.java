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

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class Controller_CreateAccount {

    @FXML
    TextField username_field;

    @FXML
    Text username_error_text;

    @FXML
    TextField password_field;

    @FXML
    TextField retype_password_field;

    @FXML
    DatePicker dob_field;

    @FXML
    ChoiceBox<String> state_dropdown;

    @FXML
    TextField city_field;

    @FXML
    Button create_account_button;

    @FXML
    public void initialize(){username_error_text.setVisible(false);}

    public void on_create_account_button(ActionEvent event) throws SQLException{
        System.out.println("Creating Account...");
    }

}
