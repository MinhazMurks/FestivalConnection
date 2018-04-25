package festival_package;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import java.text.ParseException;
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

    @FXML
    Text price_error_text;

    @FXML
    Text festival_name_invalid_character;

    @FXML
    Text festival_name_short_character;

    @FXML
    Text required_fields_empty_text;

    @FXML
    Text provider_error_text;


    /**
     * Sets fields for genre, outdoor, and camping to invisible.
     * Set visible when the appropriate festival type is selected.
     * Sets error text to invisible.
     * Adds listener to ensure only integers are inserted into the zip field
     */
    @FXML
    public void initialize()
    {




        if(Controller_Main.is_edit)
        {

            add_festival_button.setText("Save");
            name_field.setText(Controller_Main.selected_fest.name);
            zip_field.setText(Controller_Main.selected_fest.zip);
            price_field.setText(Float.toString(Controller_Main.selected_fest.price));
            start_date_field.setValue(Controller_Main.selected_fest.start_date);
            end_date_field.setValue(Controller_Main.selected_fest.end_date);

            address_field.setText(Controller_Main.selected_fest.street_address);
            city_field.setText(Controller_Main.selected_fest.city);
            state_dropdown.setValue(Controller_Main.selected_fest.state);

            if(!Controller_Main.selected_fest.providers.isEmpty())
            {
                String providers = Controller_Main.selected_fest.providers.toString();
                providers_field.setText(providers.substring(1, providers.length() - 1));
            }

            if(Controller_Main.selected_fest.type.equals("Music"))
            {

                type_dropdown.getSelectionModel().select(0);
                genre_field.setVisible(true);
                outdoor_check.setVisible(true);
                camping_check.setVisible(true);

                if(Controller_Main.selected_fest.outdoor)
                {
                    outdoor_check.fire();
                }
                if(Controller_Main.selected_fest.camping)
                {
                    camping_check.fire();
                }

                genre_field.setText(Controller_Main.selected_fest.genre);

            }
            else if(Controller_Main.selected_fest.type.equals("Art"))
            {
                type_dropdown.getSelectionModel().select(1);

                genre_field.setVisible(true);
                genre_field.setText(Controller_Main.selected_fest.genre);
            }
            else if(Controller_Main.selected_fest.type.equals("Comedy"))
            {
                type_dropdown.getSelectionModel().select(2);
            }
            else if(Controller_Main.selected_fest.type.equals("Beer"))
            {
                type_dropdown.getSelectionModel().selectLast();
            }


            Controller_Main.is_edit = false;
        }
        else
        {
            add_festival_button.setText("Add Festival");
        }

        if(type_dropdown.getSelectionModel().getSelectedIndex() != 0)
        {
            outdoor_check.setVisible(false);
            camping_check.setVisible(false);
        }
        if(type_dropdown.getSelectionModel().getSelectedIndex() != 1)
        {
            genre_field.setVisible(false);
        }


        date_error_text.setVisible(false);
        sql_error_text.setVisible(false);

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


        if(!festName.matches("[A-Za-z0-9_',~?\\s/().-]++"))
        {
            festival_name_invalid_character.setVisible(true);
            return;
        }
        if(festName.length() < 3)
        {
            festival_name_short_character.setVisible(true);
            return;
        }

        festival_name_short_character.setVisible(false);
        festival_name_invalid_character.setVisible(false);

        double price = -1;
        if (!price_field.getText().equals("")) {
            try {
                price = Double.parseDouble(price_field.getText());
                price_error_text.setVisible(false);
            }
            catch (NumberFormatException e){
                price_error_text.setVisible(true);
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
        //no longer has space, instead just by commas and trims later
        String providers = providers_field.getText();
        List<String> providerList = Arrays.asList(providers.split(","));

        for(int i = 0; i < providerList.size(); i++)
        {
            if(providerList.get(i).trim().length() >= 20)
            {
                provider_error_text.setText("Too many characters on provider number " + i + 1);
                provider_error_text.setVisible(true);
                return;
            }
            providerList.set(i, providerList.get(i).trim());
        }

        provider_error_text.setVisible(false);
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

            required_fields_empty_text.setVisible(true);
            return;
        }

        required_fields_empty_text.setVisible(false);
        if (endDate.isBefore(startDate)){
            date_error_text.setVisible(true);
            return;
        }


        //update database
        if(add_festival_button.getText().equals("Save"))
        {
            System.out.println("Saving");

            try {
                Database.update_festival(Controller_Main.selected_fest.festID, Controller_Main.selected_fest.type, festName, type, startDate.toString(), endDate.toString(), price, address, city, state, zip, providerList, genre, outdoor, camping);

                    Database.refresh_festivals();
                    Database.reset_view_list("Festival");
                    Controller_Main.listProperty_main.set(FXCollections.observableArrayList(Database.viewed_list));




            } catch (SQLException e) {
                sql_error_text.setVisible(true);
                e.printStackTrace();
            }

            stage.close();
            add_festival_button.setText("Add Festival");
            return;
        }

        //Insert into database
        try{
            Database.insertNewFestival(festName, type, startDate.toString(), endDate.toString(), price, address, city, state, zip, providerList, genre, outdoor, camping);
            Database.refresh_festivals();
            Database.reset_view_list("Festival");
            Controller_Main.listProperty_main.set(FXCollections.observableArrayList(Database.viewed_list));
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
                outdoor_check.setSelected(false);
                camping_check.setVisible(false);
                camping_check.setSelected(false);
                break;
            case "Comedy":
                genre_field.clear();
                genre_field.setVisible(false);
                outdoor_check.setVisible(false);
                outdoor_check.setSelected(false);
                camping_check.setVisible(false);
                camping_check.setSelected(false);
                break;
            case "Beer":
                genre_field.clear();
                genre_field.setVisible(false);
                outdoor_check.setVisible(false);
                outdoor_check.setSelected(false);
                camping_check.setVisible(false);
                camping_check.setSelected(false);
                break;
        }
    }
}
