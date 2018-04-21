package festival_package;


import java.time.*;
import java.util.Date;

public class User {

    String userID;
    String user_name;
    LocalDate birth_date;
    String user_location;
    boolean is_company;


    User(String userID, String user_name, boolean is_company)
    {
        this.userID = userID;
        this.user_name = user_name;
        this.is_company = is_company;
    }

    User(String userID, String user_name, LocalDate birth_date, String user_location, boolean is_company)
    {
        this.userID = userID;
        this.birth_date = birth_date;
        this.user_location = user_location;
        this.user_name = user_name;
        this.is_company = is_company;
    }


    @Override
    public String toString()
    {
        return userID + " " + user_name + " " + user_location + " " + birth_date + " " + is_company;
    }




}
