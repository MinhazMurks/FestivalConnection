package sample;

public class User {

    String userID;
    String user_name;
    String birth_date;
    String user_location;
    boolean is_company;


    User(String userID, String user_name, boolean is_company)
    {
        this.userID = userID;
        this.user_name = user_name;
        this.is_company = is_company;
    }
}
