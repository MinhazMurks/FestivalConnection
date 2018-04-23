package festival_package;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class Controller_AdvancedSearch {

    @FXML
    ChoiceBox fest_or_user;

    @FXML
    ChoiceBox dependent_options;

    @FXML
    public void initialize(){

        fest_or_user.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int oddValue = fest_or_user.getSelectionModel().getSelectedIndex() + 1;
                System.out.print(oddValue + "\n");
                if(oddValue == 1){
                    dependent_options.getItems().clear();
                    dependent_options.getItems().setAll("Username","Production Company");

                }
                else{
                    dependent_options.getItems().clear();
                    dependent_options.getItems().setAll("Location","Type", "Name");
                }


            }
        });
    }

    public void on_search_button(ActionEvent event)
    {
        System.out.println("Fuuuuck!");
    }
}
