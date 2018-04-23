package festival_package;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
import java.util.List;
import java.util.Arrays;

public class Controller_AddFestival {

    @FXML
    AnchorPane add_festival_anchor;

    @FXML
    TextField name_field;

    @FXML
    ComboBox<String> type_dropdown;

    @FXML
    DatePicker start_date_field;

    @FXML
    DatePicker end_date_field;

    @FXML
    Text date_error_text;

    @FXML
    TextField address_field;

    @FXML
    TextField price_field;

    @FXML
    TextField city_field;

    @FXML
    ChoiceBox<String> state_dropdown;

    @FXML
    TextField zip_field;

    @FXML
    TextField genre_field;

    @FXML
    TextField providers_field;

    @FXML
    CheckBox outdoor_check;

    @FXML
    CheckBox camping_check;

    @FXML
    Button add_festival_button;

    @FXML
    Text sql_error_text;

    /**
     * Sets fields for genre, outdoor, and camping to invisible.
     * Set visible when the appropriate festival type is selected.
     * Sets error text to invisible.
     * Adds listener to ensure only integers are inserted into the zip field
     */
    @FXML
    public void initialize(){
        genre_field.setVisible(false); outdoor_check.setVisible(false); camping_check.setVisible(false); date_error_text.setVisible(false); sql_error_text.setVisible(false);

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
     * Checks that fields are filled out properly.
     * Passes values from fields to the insertNewFestival method in Database.java.
     * @param event
     */
    public void on_add_festival_button(ActionEvent event){
        //Reset error messages
        date_error_text.setVisible(false); sql_error_text.setVisible(false);
        //Get the stage so we can close the window later
        Stage stage = (Stage) add_festival_anchor.getScene().getWindow();

        String festName = name_field.getText();
        String type = type_dropdown.getValue();
        LocalDate startDate = start_date_field.getValue();
        LocalDate endDate = end_date_field.getValue();
        double price = -1;
        if (!price_field.getText().equals("")) {
            try {
                price = Double.parseDouble(price_field.getText());
            }
            catch (NumberFormatException e){
                e.printStackTrace();
                return;
            }
        }
        String address = address_field.getText();
        String city = city_field.getText();
        String state = state_dropdown.getValue();
        int zip = -1;
        if (!zip_field.getText().equals("")) {
            if ((zip_field.getText().length() == 5)){
                zip = Integer.parseInt(zip_field.getText());
            }
        }

        //Create list of providers using String.split(",[ ]*") to erase both commas and spaces
        String providers = providers_field.getText();
        List<String> providerList = Arrays.asList(providers.split(",[ ]*"));

        String genre = genre_field.getText();

        boolean outdoor = outdoor_check.isSelected();
        boolean camping = camping_check.isSelected();

        //Perform checks
        if ((festName.equals("")) ||
                (address.equals("")) ||
                (city.equals("")) ||
                (state.equals("")) ||
                (genre.equals("")) ||
                (price < 0) ||
                (zip < 0)){
            return;
        }
        if (endDate.isBefore(startDate)){
            date_error_text.setVisible(true);
            return;
        }

        //Insert into database
        try{
            Database.insertNewFestival(festName, type, startDate.toString(), endDate.toString(), price, address, city, state, zip, providerList, genre, outdoor, camping);
            Database.refresh_festivals();
            stage.close();
        }
        catch (SQLException e){
            sql_error_text.setVisible(true);
            e.printStackTrace();
            return;
        }
    }

    /**
     * sets the visibility of genre, outdoor, and camping based on the value selected in the
     * type_dropdown combo box
     * @param event
     */
    public void on_type_dropdown(ActionEvent event){
        String type = type_dropdown.getValue();

        switch (type){
            case "Music":
                genre_field.setVisible(true);
                outdoor_check.setVisible(true);
                camping_check.setVisible(true);
                break;
            case "Art":
                genre_field.setVisible(true);
                outdoor_check.setVisible(false);
                camping_check.setVisible(false);
                break;
            case "Comedy":
                genre_field.setVisible(false);
                outdoor_check.setVisible(false);
                camping_check.setVisible(false);
                break;
            case "Beer":
                genre_field.setVisible(false);
                outdoor_check.setVisible(false);
                camping_check.setVisible(false);
        }
    }
}
