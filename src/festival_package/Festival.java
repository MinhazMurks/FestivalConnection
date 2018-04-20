package festival_package;

public class Festival {

    private String festID;
    private String location;
    private String production_comp;
    private String start_date;
    private String end_date;
    private float price;

    Festival(String festID, String production_comp,float price)
    {
        this.festID = festID;
        this.production_comp = production_comp;
        this.price = price;
    }

}
