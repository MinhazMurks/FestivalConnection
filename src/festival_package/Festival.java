package festival_package;

import java.time.*;
import java.util.ArrayList;

public class Festival {


    ArrayList<String> providers = new ArrayList<>();

    String festID;
    String location;
    String production_comp;
    String type;
    LocalDate start_date;
    LocalDate end_date;
    float price;

    //not every festival has the below attributes:
    String genre = "";
    boolean camping = false;
    boolean outdoor = false;


    Festival(String festID, String location, String production_comp, String type, LocalDate start_date, LocalDate end_date, float price)
    {
        this.festID = festID;
        this.location = location;
        this.production_comp = production_comp;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
    }

    Festival(String festID, String location, String production_comp, String type, LocalDate start_date, LocalDate end_date, float price, String genre)
    {
        this.festID = festID;
        this.location = location;
        this.production_comp = production_comp;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
        this.genre = genre;
    }

    Festival(String festID, String location, String production_comp, String type, LocalDate start_date, LocalDate end_date, float price, String genre, boolean camping, boolean outdoor)
    {
        this.festID = festID;
        this.location = location;
        this.production_comp = production_comp;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
        this.genre = genre;
        this.camping = camping;
        this.outdoor = outdoor;
    }



    boolean providers_empty()
    {
        return this.providers.isEmpty();
    }

    boolean contains_provider(String provider)
    {
        return this.providers.contains(provider);
    }

}
