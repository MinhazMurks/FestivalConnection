package festival_package;


import com.sun.xml.internal.ws.util.StringUtils;

import java.time.*;
import java.util.Date;

public class User {

    String userID;
    String user_name;
    LocalDate birth_date;
    String user_location;
    boolean is_company;


    User(String user_name)
    {
        this.user_name = user_name;
    }

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


    public String description()
    {
        String result = "";

        if(this.is_company)
        {
            result = result.concat("Production Company" + '\n');
        }


        result = result.concat(this.user_name + "\n" + user_location);

        if(this.is_company)
        {
            result = result.concat("\n Established " + StringUtils.capitalize(birth_date.getMonth().toString().toLowerCase()) + " " + birth_date.getDayOfMonth() + ", " + birth_date.getYear());
        }
        else
        {
            result = result.concat("\n" + StringUtils.capitalize(birth_date.getMonth().toString().toLowerCase()) + " " + birth_date.getDayOfMonth() + ", " + birth_date.getYear());
        }

        return result;
    }

    @Override
    public String toString()
    {
        return userID + " " + user_name + " " + user_location + " " + birth_date.toString() + " " + is_company;
    }


    @Override
    public boolean equals(Object other)
    {
        if(other == null)
        {
            return false;
        }

        if(!User.class.isAssignableFrom(other.getClass()))
        {
            return false;
        }

        User other_user = (User)other;

        if(!this.userID.equals(other_user.userID))
        {
            return false;
        }
        if(!this.user_name.equals(other_user.user_name))
        {
            return false;
        }


        return true;
    }

}
