package festival_package;

import java.time.*;
import java.util.ArrayList;

public class Festival {


    ArrayList<String> providers = new ArrayList<>();

    String festID;
    String name;
    String location = "";
    String production_comp = "";
    String type;
    LocalDate start_date;
    LocalDate end_date;
    float price;

    //not every festival has the below attributes:
    String genre = "";
    boolean camping = false;
    boolean outdoor = false;


    Festival(String festID, String name, String location, String production_comp, String type, LocalDate start_date, LocalDate end_date, float price)
    {
        this.festID = festID;
        this.name = name;
        this.location = location;
        this.production_comp = production_comp;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
    }

    Festival(String festID, String name, String location, String production_comp, String type, LocalDate start_date, LocalDate end_date, float price, String genre)
    {
        this.festID = festID;
        this.name = name;
        this.location = location;
        this.production_comp = production_comp;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
        this.genre = genre;
    }

    Festival(String festID, String name, String location, String production_comp, String type, LocalDate start_date, LocalDate end_date, float price, String genre, boolean camping, boolean outdoor)
    {
        this.festID = festID;
        this.name = name;
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


    @Override
    public boolean equals(Object other)
    {
        if(other == null)
        {
            return false;
        }

        if(!Festival.class.isAssignableFrom(other.getClass()))
        {
            return false;
        }

        Festival other_festival = (Festival)other;

        if(!this.festID.equals(other_festival.festID))
        {
            return false;
        }
        if(!this.type.equals(other_festival.type))
        {
            return false;
        }
        if(!this.name.equals(other_festival.name))
        {
            return false;
        }


        return true;
    }


    public String description()
    {
        String result = this.name + ": " + this.type + " Festival";

        if(!this.location.equals(""))
        {
            result += "\n " + this.location;
        }

        result += "\n From: " + start_date + " to " + this.end_date;
        result += "\n Ticket cost: $" + price;

        return result;
    }
}
