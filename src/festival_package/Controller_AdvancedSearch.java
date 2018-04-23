package festival_package;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    TextArea providers_field;

    @FXML
    public void initialize(){

        genre_field.setVisible(false);
        outdoor_check.setVisible(false);
        camping_check.setVisible(false);
        city_field.setVisible(false);
        providers_field.setWrapText(true);

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

    public void on_advanced_search_button(ActionEvent event) throws SQLException, ParseException {

        String columns = "SELECT festival.name, festival.festID, festival.fest_type, festival.start_date, " +
                "festival.end_date, festival.price";
        String tables = " from festival";
        String clauses = "";

        String type = "";

        if(providers_field.getText().trim().length() > 0){
            tables += " JOIN providers on festival.festID = providers.festID ";
            columns += ", providers.name";
        }

        if(state_dropdown.getSelectionModel().getSelectedIndex() != -1){
            columns += ", location.state";
            tables +=  "JOIN location on festival.festID = providers.festID ";
            if(city_field.getText().trim().length() >  0){
                columns += "location.city";
            }
        }
        boolean isEmpty = true;
        if(name_field.getText().trim().length() > 0){
            clauses += " where festival.name  like  '%" + name_field.getText().trim() + "%'";
            isEmpty = false;

        }


        if(fest_type_dropdown.getSelectionModel().getSelectedIndex() != -1){
            if(isEmpty == false)
            {
                clauses += " AND ";
            }
            else
            {
                clauses += " WHERE ";
            }
            String fest_type = fest_type_dropdown.getValue();
            type = fest_type;

            if(fest_type.equals("Music") | fest_type.equals("Art"))
            {
                columns += ", " + fest_type + ".genre";

                if(fest_type.equals("Music"))
                {
                    columns += ", Music.camping, Music.outdoor";
                }
            }
            tables += " join " + fest_type + " ON festival.festID =" + fest_type + ".festID";
            clauses += " festival.fest_type = '" + fest_type + "'";
            isEmpty = false;
        }

        if(price_field.getText().trim().length() > 0){
            if(isEmpty == false)
            {
                clauses += " AND ";
            }
            else
            {
                clauses += " WHERE ";
            }
            String price = price_field.getText();
            clauses += " festival.price <= " + price;
            isEmpty = false;
        }

        if(startDate_field.getValue()!= null){
            if(isEmpty == false)
            {
                clauses += " AND ";
            }
            else
            {
                clauses += " WHERE ";
            }
            LocalDate start = startDate_field.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
            String start_string = start.format(formatter);
            clauses += " festival.start_date <= " + "'" + start_string + "'";
            isEmpty = false;
        }

        if(endDate_field.getValue()!= null){
            if(isEmpty == false)
            {
                clauses += " AND ";
            }
            else
            {
                clauses += " WHERE ";
            }
            LocalDate end = endDate_field.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
            String end_string = end.format(formatter);
            clauses += " festival.end_date <= " + "'" + end_string + "'";
            isEmpty = false;
        }

        if(state_dropdown.getSelectionModel().getSelectedIndex() != -1) {
            if (isEmpty == false) {
                clauses += " AND ";
            } else {
                clauses += " WHERE ";
            }
            String state = state_dropdown.getValue();
            String city = city_field.getText();

            clauses += " location.state = '" + state + "' and location.city LIKE '%" + city +"%'";
            isEmpty = false;
        }

        if(genre_field.getText().trim().length() > 0 && (type.equals("Music") | type.equals("Art"))){
            if (isEmpty == false) {
                clauses += " AND ";
            } else {
                clauses += " WHERE ";
            }
            String genre = genre_field.getText();
            clauses += " .genre  like  '% " + genre + "%'";

            if(type.equals("Music"))
            {
                int outdoor_check_int = outdoor_check.isSelected()? 0:1;
                int camping_check_int = camping_check.isSelected()? 0:1;
                clauses += " Music.outdoor = " + Integer.toString(outdoor_check_int) + " AND Music.camping = " + Integer.toString(camping_check_int);
            }

            isEmpty = false;
        }

        if(providers_field.getText().trim().length() > 0)
        {


            String unparsed_providers = providers_field.getText();

            List<String> providers = new ArrayList<String>(Arrays.asList(unparsed_providers.split(",")));

            for(int i = 0; i < providers.size(); i++)
            {
                providers.set(i, providers.get(i).trim());
                if(i == 0)
                {
                    if (isEmpty == false) {
                        clauses += " AND ";
                    } else {
                        clauses += " WHERE ";
                    }
                }
                else
                {
                    clauses += " OR ";
                }

                clauses += " providers.name = '" + providers.get(i) + "'";
            }

            isEmpty = false;
        }


        if(price_order_dropdown.getSelectionModel().getSelectedIndex() != -1)
        {
            int order_index = price_order_dropdown.getSelectionModel().getSelectedIndex();

            if(order_index == 0)
            {
                clauses += " ORDER BY Festival.price ASC";
            }
            else
            {
                clauses+= " ORDER BY Festival.price DESC";
            }
        }




        String input  = columns + tables + clauses + ";";
        System.out.print(input);

        ResultSet resultSet = Database.execute_query(input);

        if(!resultSet.isBeforeFirst())
        {
            System.out.println("No results!");
            return;
        }

        Database.viewed_list.clear();
        Database.viewed_list_id.clear();

        while(resultSet.next())
        {
            Database.viewed_list.add(resultSet.getString("festival.name"));
            Database.viewed_list_id.add(resultSet.getString("festival.festID"));
        }

        //Controller_Main.listProperty_main.set(FXCollections.observableArrayList(Database.viewed_list));
        //Controller_Main.search_listview.itemsProperty().bind(Controller_Main.listProperty_main);


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
