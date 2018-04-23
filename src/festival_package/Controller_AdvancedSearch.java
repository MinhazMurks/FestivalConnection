package festival_package;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller_AdvancedSearch {

    @FXML
    TextField name_field;

    @FXML
    ComboBox<String> fest_type_dropdown;

    @FXML
    TextField price_field;

    @FXML
    ComboBox<String> price_order_dropdown;

    @FXML
    DatePicker startDate_field;

    @FXML
    DatePicker endDate_field;

    @FXML
    ComboBox<String> state_dropdown;

    @FXML
    TextField city_field;

    @FXML
    TextField genre_field;

    @FXML
    CheckBox outdoor_check;

    @FXML
    CheckBox camping_check;

    @FXML
    Button search_button;

    @FXML
    TextField providers_field;

    @FXML
    public void initialize(){

        genre_field.setVisible(false);
        outdoor_check.setVisible(false);
        camping_check.setVisible(false);
        city_field.setVisible(false);

        price_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    price_field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void on_search_button(ActionEvent event) {
    }

    public void on_state_dropdown(ActionEvent event){
        city_field.setVisible(true);
    }

    /**
     * sets the visibility of genre, outdoor, and camping based on the value selected in the
     * type_dropdown combo box
     * @param event
     */
    public void on_fest_type_dropdown(ActionEvent event){
        String type = fest_type_dropdown.getValue();

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
