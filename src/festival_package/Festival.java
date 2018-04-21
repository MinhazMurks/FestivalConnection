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

    boolean providers_empty()
    {
        return this.providers.isEmpty();
    }

    boolean contains_provider(String provider)
    {
        return this.providers.contains(provider);
    }

}
