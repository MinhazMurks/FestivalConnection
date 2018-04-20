package sample;

public class Festival {

    String festID;
    String location;
    String production_comp;
    String start_date;
    String end_date;
    float price;

    Festival(String festID, String production_comp,float price)
    {
        this.festID = festID;
        this.production_comp = production_comp;
        this.price = price;
    }

}
