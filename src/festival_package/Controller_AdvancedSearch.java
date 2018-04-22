package festival_package;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class Controller_AdvancedSearch {

    @FXML
    ChoiceBox festOrUser;

    @FXML
    ChoiceBox Optional;

    @FXML
    public void initialize(){

        festOrUser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int oddValue = festOrUser.getSelectionModel().getSelectedIndex() + 1;
                System.out.print(oddValue + "\n");
                if(oddValue == 1){
                    Optional.getItems().clear();
                    Optional.getItems().setAll("Username","Production Company");

                }
                else{
                    Optional.getItems().clear();
                    Optional.getItems().setAll("Location","Type", "Name");
                }


            }
        });
    }

    public void on_search_button(ActionEvent event)
    {
        System.out.println("Fuuuuck!");
    }
}
