package festival_package;

public class User {

    private String userID;
    private String user_name;
    private String birth_date;
    private String user_location;
    private boolean is_company;


    User(String userID, String user_name, boolean is_company)
    {
        this.userID = userID;
        this.user_name = user_name;
        this.is_company = is_company;
    }
}
