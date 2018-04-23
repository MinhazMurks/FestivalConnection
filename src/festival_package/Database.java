package festival_package;


import javax.xml.crypto.Data;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.UUID.*;



public class Database {




    //Globals
    public static ArrayList<Festival> Festivals = new ArrayList<>();
    public static ArrayList<String> Festivals_Names = new ArrayList<>();

    public static ArrayList<User> Users = new ArrayList<>();
    public static ArrayList<String> User_Names = new ArrayList<>();

    public static ArrayList<String> viewed_friends_names = new ArrayList<>();
    public static ArrayList<String> viewed_friends_id = new ArrayList<>();

    public static ArrayList<String> Locations = new ArrayList<>();

    public static ArrayList<String> test_values = new ArrayList<>();

    public static ArrayList<String> viewed_list = new ArrayList<>();
    public static ArrayList<String> viewed_list_id = new ArrayList<>();


    public static User cur_user = null;

    static private Connection connection;

    static {
        try {
            connection = DriverManager.getConnection
                        (
                                "jdbc:mysql://festivalproject.mysql.database.azure.com/festivals_project",
                                "festival_admin@festivalproject",
                                "muK43her"
                        );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries the DB for a given username and returns the guid for that user if found.
     * Returns an empty String "" if the username is not found
     * TODO: Add password authentication
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public static boolean authenticate(String username, String password) throws SQLException, ArrayIndexOutOfBoundsException
    {

        String query = ("SELECT userID, password " +
                "FROM Users " +
                "WHERE user_name = '" + username + "' and password = '" + password + "';");
        System.out.println(query);

        String guid = "";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            guid = resultSet.getString("userID");
            System.out.println(guid);
        }

        cur_user = Database.Users.get(Database.Users.indexOf(user_from_userID(guid)));

        resultSet.close();

        if(cur_user != null)
        {
            return true;
        }

        return false;

    }
    /**
     * Queries the database for a given username. If any results are returned in resultSet, returns false.
     * @param username
     * @return
     * @throws SQLException
     */
    public static boolean usernameAvailable(String username) throws SQLException
    {
        String query = ("SELECT user_name " +
                "FROM Users " +
                "WHERE user_name = '" + username + "';");
        System.out.println(query);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            return false;
        }
        return true;
    }
    /**
     * Adds a row to both Users and Location. A single UUID is created and used in both tables.
     * @param username
     * @param password
     * @param dob
     * @param state
     * @param city
     * @param address
     * @param zip
     */
    public static void insertNewUser(String username, String password, String dob, String state, String city, String address, int zip) throws SQLException
    {
        String guid = UUID.randomUUID().toString();
        String insertUserSQL = ("INSERT INTO Users VALUES ('" + guid + "', '" + username + "', '" + dob + "', " + 0 + ", '" + password + "');");
        String insertLocationSQL = ("INSERT INTO Location VALUES ('" + guid + "', NULL, '"  +  city + "', '" + state + "', '" + address + "', " + zip + ");");

        System.out.println(insertUserSQL);
        System.out.println(insertLocationSQL);

        Statement statement = connection.createStatement();
        statement.executeUpdate(insertUserSQL);
        statement.executeUpdate(insertLocationSQL);
    }

    public static void insertNewFestival(String festName, String type, String startDate, String endDate, double price, String address, String city, String state, int zip, List<String> providers, String genre, boolean outdoor, boolean camping) throws SQLException
    {
        String guid = UUID.randomUUID().toString();
        String insertFestivalSQL = ("INSERT INTO Festival VALUES ('" + guid + "', '" + type + "', '" + festName + "', '" + cur_user.userID + "', '" + startDate + "', '" + endDate + "', " + price + ");");
        String insertLocationSQL = ("INSERT INTO Location VALUES (NULL" + ", '" + guid + "', '" +  city + "', '" + state + "', '" + address + "', " + zip + ");");

        Statement statement = connection.createStatement();

        System.out.println(insertFestivalSQL);
        statement.executeUpdate(insertFestivalSQL);
        System.out.println(insertLocationSQL);
        statement.executeUpdate(insertLocationSQL);

        for (String str: providers){
            String insertProviderSQL = ("INSERT INTO Providers VALUES ('" + guid + "', '" + str + "');");
            System.out.println(insertProviderSQL);
            statement.executeUpdate(insertProviderSQL);
        }

        String insertSubTypeSQL = "";
        switch (type) {
            case "Music":
                int out = 0;
                int camp = 0;
                if (outdoor) {
                    out = 1;
                }
                if (camping) {
                    camp = 1;
                }
                insertSubTypeSQL = ("INSERT INTO Music VALUES ('" + guid + "', '" + genre + "', " + out + ", " + camp + ");");
                System.out.println(insertSubTypeSQL);
                statement.executeUpdate(insertSubTypeSQL);
                break;
            case "Art":
                insertSubTypeSQL = ("INSERT INTO Art VALUES ('" + guid + "', '" + genre + "');");
                System.out.println(insertSubTypeSQL);
                statement.executeUpdate(insertSubTypeSQL);
                break;
            case "Comedy":
                insertSubTypeSQL = ("INSERT INTO Comedy VALUES ('" + guid + "');");
                System.out.println(insertSubTypeSQL);
                statement.executeUpdate(insertSubTypeSQL);
                break;
            case "Beer":
                insertSubTypeSQL = ("INSERT INTO Beer VALUES ('" + guid + "');");
                System.out.println(insertSubTypeSQL);
                statement.executeUpdate(insertSubTypeSQL);
                break;
        }
    }

    /**
     * Takes the current user_guid and queries the Location table for it.
     * TODO: ADD CONDITION CHECKS FOR INCORRECT OR NONEXISTENT GUID
     * @return The concatenated city and state columns corresponding with current guid
     */
    public static String getUserLocation() throws SQLException
    {
        String query = ("SELECT city, state " +
                "FROM Location " +
                "WHERE userID = '" + cur_user.userID + "';");
        System.out.println(query);

        String userLocation = "";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            userLocation += resultSet.getString("city");
            userLocation += ", ";
            userLocation += resultSet.getString("state");
        }

        System.out.println(userLocation);
        return userLocation;
    }
    /**
     * Takes in a Fesival's guid and gives back the Fesival's full address
     * in the form "[Street Address], [City], [State] [Zip]"
     * TODO:ADD CONDITION CHECKS FOR INCORRECT OR NONEXISTENT GUID
     * @param guid A festival's guid
     * @return A string of the above form
     * @throws SQLException
     */
    public static String getFestivalLocation(String guid) throws SQLException
    {
        String query = ("SELECT streetAddress, city, state, zip " +
                "FROM Location " +
                "WHERE festID = '" + guid + "';");
        System.out.println(query);

        String festLocation = "";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            festLocation += resultSet.getString("streetAddress");
            festLocation += ", ";
            festLocation += resultSet.getString("city");
            festLocation += ", ";
            festLocation += resultSet.getString("state");
            festLocation += " ";
            festLocation += resultSet.getInt("zip");
        }

        System.out.println(festLocation);
        return festLocation;
    }
    public static void insert_to_table(Statement statement, String table, String value) throws SQLException
    {

        String sql = "INSERT INTO " + table + " VALUES ( \'" + value + "\');";
        System.out.println("Query: " + sql);

        statement.executeUpdate(sql);
    }
    public static void insert_to_table(String table, ArrayList<String> values) throws SQLException
    {

        String sql = "INSERT INTO " + table + " VALUES (";

        for(int i = 0; i < values.size(); i++)
        {
            sql += "\'" + values.get(i) + "\'";

            if((i + 1) < values.size())
            {
                sql += ", ";
            }
        }

        sql += ");";

        System.out.println("Query: " + sql);

        Statement statement = connection.createStatement();

        statement.executeUpdate(sql);
    }
    public static String remove_defs(String table)
    {
        int length = table.length();
        int i = length - 1;

        int last_paranthesis_index = i;
        int first_paranthesis_index = -1;

        for(; i >= 0; i--)
        {
            if(table.charAt(i) == '(')
            {
                first_paranthesis_index = i;
                break;
            }
        }

        return table.substring(0, first_paranthesis_index);
    }
    public static String fix_title(String word)
    {
        String result = word;


        if(result.endsWith(".fxml"))
        {
            result = result.substring(0, result.indexOf(".fxml"));
        }
        if(result.contains("_"))
        {
            result = result.replace('_', ' ');
        }


        for(int i = 0; i < result.length(); i++)
        {
            if(Character.isLowerCase(result.charAt(i)) && i + 1 < result.length() && Character.isUpperCase(result.charAt(i + 1)))
            {
                System.out.println("character: " + result.charAt(i) + " is lowercase and " + result.charAt(i + 1) + " is uppercase");
                System.out.println("Old result: " + result);
                result = word.substring(0, i + 1) + " " + result.substring(i + 1, result.length());
                System.out.println("New Result: " + result);
            }

        }

        return result;
    }
    public static ResultSet select_from_table(ArrayList<String> columns, String table) throws SQLException
    {

        String new_table = table;

        if(table.endsWith(")"))
        {
            new_table = remove_defs(table);
        }


        String query = "SELECT ";
        int i;

        for(i = 0; i < columns.size(); i++)
        {
            query = query.concat(columns.get(i));

            if(i + 1 != columns.size())
            {
                query += ", ";
            }
        }

        query = query.concat("\nFROM " + new_table + ";");

        System.out.println("Query: " + query);

        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
    public static ResultSet select_from_table(String column, String table) throws SQLException
    {

        String new_table = table;

        if(table.endsWith(")"))
        {
            new_table = remove_defs(table);
        }



        String sql = "SELECT " + column + " FROM " + new_table + ";";

        System.out.println("Query: " + sql);

        Statement statement = connection.createStatement();

        return statement.executeQuery(sql);
    }
    public static ResultSet select_all_from_table(Statement statement, String table) throws SQLException
    {

        String new_table = table;

        if(table.endsWith(")"))
        {
            new_table = remove_defs(table);
        }


        String sql = "SELECT *";

        sql += "\nFROM " + new_table + ";";

        System.out.println("Query: " + sql);
        return statement.executeQuery(sql);
    }
    public static LocalDate date_from_string(String date)
    {
        return LocalDate.parse(date);
    }
    public static User user_from_user_name(String username)
    {
        User result;

        User temp = null;

        for(int i = 0; i < Users.size(); i++)
        {
            if(Users.get(i).user_name.equals(username))
            {
                temp = Users.get(i);
            }
        }

        result = Users.get(Users.indexOf(temp));

        return result;
    }
    public static User user_from_userID(String userID) throws ArrayIndexOutOfBoundsException
    {
        User result;

        User temp = null;

        for(int i = 0; i < Users.size(); i++)
        {
            if(Users.get(i).userID.equals(userID))
            {
                temp = Users.get(i);
            }
        }

        //System.out.println("Temp user is: " + temp + " and userID: " + userID);
        result = Users.get(Users.indexOf(temp));

        return result;
    }
    public static Festival fest_from_festID(String festID)
    {
        Festival result = null;
        for(int i = 0; i < Festivals.size(); i++)
        {
            if(Festivals.get(i).festID.equals(festID))
            {
                result = Festivals.get(i);
            }
        }

        return result;
    }
    public static void refresh_lists() throws SQLException, ParseException
    {
        refresh_users();
        refresh_friends();
        refresh_user_friends();
        refresh_festivals();
        refresh_bookmarks();
    }
    public static void refresh_users() throws SQLException, ParseException
    {
        Users.clear();

        ArrayList<String> columns = new ArrayList<>();

        String user_table = "users";
        String location_table = "location";

        columns.add("users.userId");
        columns.add("users.user_name");
        columns.add("users.birthdate");
        columns.add("users.is_company");

        columns.add("location.city");
        columns.add("location.state");


        String query = "SELECT ";

        for(int i = 0; i < columns.size(); i++)
        {
            query += columns.get(i);

            if((i + 1) < columns.size())
            {
                query += " ,";
            }
        }

        query += " FROM " + user_table;

        query += "\n JOIN " + location_table + " ON " + user_table + ".userID = " + location_table + ".userID;";

        Statement statement = connection.createStatement();
        ResultSet rows_user_IDs = statement.executeQuery(query);
        System.out.println("Query: " + query);



        while(rows_user_IDs.next())
        {
            LocalDate birth_date = date_from_string(rows_user_IDs.getDate("birthdate").toString());

            System.out.println("Row: " + rows_user_IDs.getString("user_name") + " " + rows_user_IDs.getString("city") + ", " + rows_user_IDs.getString("state"));

            User cur_user = new User(rows_user_IDs.getString("userID"),
                    rows_user_IDs.getString("user_name"),
                    birth_date,
                    rows_user_IDs.getString("city") + ", " + rows_user_IDs.getString("state"),
                    rows_user_IDs.getBoolean("is_company"));

            Users.add(cur_user);
            System.out.println("Added user: " + cur_user.toString());
        }


        rows_user_IDs.close();
        re_add_user_names();

        System.out.println("Users: " + User_Names.toString());

    }
    public static void re_add_user_names()
    {
        User_Names.clear();

        for(int i = 0; i < Users.size(); i++)
        {
            User_Names.add(Users.get(i).user_name);
        }
    }
    public static void refresh_festivals()
    {
        Festivals.clear();

        ArrayList<String> columns = new ArrayList<>();

        columns.add("festival.festID");
        columns.add("festival.name");
        columns.add("festival.production_comp");
        columns.add("festival.fest_type");
        columns.add("festival.start_date");
        columns.add("festival.end_date");
        columns.add("festival.price");

        columns.add("music.genre");
        columns.add("music.outdoor");
        columns.add("music.camping");

        columns.add("art.genre");


        String festival_table = "festival";

        columns.add("location.streetAddress");
        columns.add("location.city");
        columns.add("location.state");
        columns.add("location.zip");


        //all the sub-tables of festival
        String art_table = "art";
        String beer_table = "beer";
        String comedy_table = "comedy";
        String music_table = "music";
        String location_table = "location";


        columns.add("providers.name");
        String providers_table = "providers";

        String query = "SELECT ";

        for(int i = 0; i < columns.size(); i++)
        {
            query += columns.get(i);

            if((i + 1) < columns.size())
            {
                query += " ,";
            }
        }

        query += " FROM " + festival_table;

        query += "\n LEFT JOIN " + art_table + " ON " + festival_table + ".festID = " + art_table + ".festID";
        query += "\n LEFT JOIN " + beer_table + " ON " + festival_table + ".festID = " + beer_table + ".festID";
        query += "\n LEFT JOIN " + comedy_table + " ON " + festival_table + ".festID = " + comedy_table + ".festID";
        query += "\n LEFT JOIN " + music_table + " ON " + festival_table + ".festID = " + music_table + ".festID";
        query += "\n JOIN " + providers_table + " ON " + festival_table + ".festID = " + providers_table + ".festID";
        query += "\n JOIN " + location_table + " ON " + festival_table + ".festID = " + location_table + ".festID";


        query += ";";

        System.out.println("Query: " + query);

        Statement statement = null;
        try {
            statement = connection.createStatement();


            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("num rows: " + resultSet.getFetchSize());

            while (resultSet.next()) {
                //System.out.println("I got to the loop");

                LocalDate start_date = date_from_string(resultSet.getDate("start_date").toString());
                LocalDate end_date = date_from_string(resultSet.getDate("end_date").toString());

                System.out.println("before creating temp fest");

                Festival temp = new Festival(resultSet.getString("festival.festID"),
                        resultSet.getString("festival.name"),
                        resultSet.getString("location.streetAddress") + ", " +
                                resultSet.getString("location.city") + ", " +
                                resultSet.getString("state") + " " +
                                resultSet.getString("zip"),
                        resultSet.getString("production_comp"),
                        resultSet.getString("festival.fest_type"),
                        start_date, end_date,
                        resultSet.getFloat("festival.price"));

                System.out.println("Temp fest: " + temp.toString());


                if (Festivals.contains(temp)) {
                    Festivals.get(Festivals.indexOf(temp)).providers.add(resultSet.getString("name"));
                    continue;
                } else if (temp.type.equals("Music")) {
                    temp.genre = resultSet.getString("genre");
                    temp.outdoor = resultSet.getBoolean("outdoor");
                    temp.camping = resultSet.getBoolean("camping");
                    temp.providers.add(resultSet.getString("name"));
                } else if (temp.type.equals("Comedy")) {
                    temp.providers.add(resultSet.getString("name"));
                } else if (temp.type.equals("Art")) {
                    temp.genre = resultSet.getString("genre");
                    temp.providers.add(resultSet.getString("name"));
                } else if (temp.type.equals("Beer")) {
                    temp.providers.add(resultSet.getString("name"));
                }

                Festivals.add(temp);
            }
            //System.out.println("do i get here");
            re_add_fest_names();

        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void re_add_fest_names()
    {
        Festivals_Names.clear();

        for(int i = 0; i < Festivals.size(); i++)
        {
            Festivals_Names.add(Festivals.get(i).name);
        }
    }
    public static void set_viewed_list(ArrayList<?> new_list)
    {
        viewed_list.clear();
        viewed_list_id.clear();


        if(!new_list.isEmpty())
        {
            if(Festival.class.isAssignableFrom(new_list.get(0).getClass()))
            {
                for(int i = 0; i < new_list.size(); i++)
                {
                    viewed_list.add(((Festival)new_list.get(i)).name);
                    viewed_list_id.add(((Festival)new_list.get(i)).festID);
                }
            }
            else
            {
                for(int i = 0; i < new_list.size(); i++)
                {
                    viewed_list.add(((User)new_list.get(i)).user_name);
                    viewed_list_id.add(((User)new_list.get(i)).userID);
                }
            }
        }


        System.out.println("New list: " + viewed_list.toString());

    }
    public static void name_search(String search_val, String search_type) throws SQLException, ParseException
    {

        Database.refresh_lists();

        viewed_list.clear();
        viewed_list_id.clear();

        String name_col = "";
        String id_col = "";

        String query = "SELECT ";

        if(search_type.equals("User"))
        {
            query += "users.user_name, users.userID from users where users.user_name LIKE '%" + search_val + "%';";
            name_col = "users.user_name";
            id_col = "users.userID";
        }
        else if(search_type.equals("Festival"))
        {
            query += "festival.name, festival.festID from festival where festival.name LIKE '%" + search_val + "%';";
            name_col = "festival.name";
            id_col = "festival.festID";
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);


        while(resultSet.next())
        {
            viewed_list.add(resultSet.getString(name_col));
            viewed_list_id.add(resultSet.getString(id_col));
        }
    }
    public static void reset_view_list(String type)
    {
        viewed_list.clear();
        viewed_list_id.clear();

        if(type.equals("User"))
        {
            for(int i = 0; i < Users.size(); i++)
            {
                viewed_list.add(Users.get(i).user_name);
                viewed_list_id.add(Users.get(i).userID);
            }
        }
        else //then Festival
        {
            for(int i = 0; i < Festivals.size(); i++)
            {
                viewed_list.add(Festivals.get(i).name);
                viewed_list_id.add(Festivals.get(i).festID);
            }
        }
    }
    public static void refresh_friends() throws SQLException
    {
        for(int i = 0; i < Users.size(); i++)
        {
            Users.get(i).Friends.clear();
            Users.get(i).Friend_Names.clear();

            String query = "SELECT * FROM Friends;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if(!resultSet.isBeforeFirst())
            {
                System.out.println("Empty Friends!");
            }

            while(resultSet.next())
            {
                User first_user = user_from_userID(resultSet.getString("user1"));
                User second_user = user_from_userID(resultSet.getString("user2"));

                System.out.println("first_user: " + first_user.user_name);
                System.out.println("second_user: " + second_user.user_name);

                Users.get(Users.indexOf(first_user)).add_friend(second_user);
            }

            cur_user = Users.get(Users.indexOf(cur_user));
            refresh_user_friends();

        }
    }
    public static void refresh_bookmarks() throws SQLException
    {
        for (int i = 0; i < Users.size(); i++)
        {
            Users.get(i).Bookmarks.clear();
            Users.get(i).Bookmark_Names.clear();

            String query = "SELECT * FROM bookmarks;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Empty Bookmarks!");
            }

            while (resultSet.next()) {
                User first_user = user_from_userID(resultSet.getString("userID"));
                Festival bookmarked = fest_from_festID(resultSet.getString("festID"));

                System.out.println("first_user: " + first_user.user_name);
                System.out.println("Festival: " + bookmarked.name);

                Users.get(Users.indexOf(first_user)).add_bookmark(bookmarked);
            }

            cur_user = Users.get(Users.indexOf(cur_user));

        }
    }
    public static void remove_friend(String friend_ID) throws SQLException
    {
        String query = "call delete_friend(" + "\'" + cur_user.userID + "\'" + ", \'" + friend_ID + "\');";

        System.out.println("Query: " + query);

        Statement statement = connection.createStatement();
        statement.executeQuery(query);

        refresh_friends();
    }
    public static void remove_bookmark(String fest_ID) throws SQLException
    {
        String query = "call delete_bookmark(" + "\'" + cur_user.userID + "\'" + ", \'" + fest_ID + "\');";

        System.out.println("Query: " + query);

        Statement statement = connection.createStatement();
        statement.executeQuery(query);

        refresh_bookmarks();
    }
    public static void refresh_user_friends()
    {
        viewed_friends_names.clear();
        viewed_friends_id.clear();

        for(int i = 0; i < cur_user.Friends.size(); i++)
        {
            String user_name = cur_user.Friends.get(i).user_name;
            String user_id = cur_user.Friends.get(i).userID;

            if(!viewed_friends_names.contains(user_name))
            {
                viewed_friends_names.add(user_name);
            }
            if(!viewed_list_id.contains(user_id))
            {
                viewed_friends_id.add(user_id);
            }

        }
    }
    public static void search_friends(String search_val) throws SQLException
    {
        String query = "call search_friends(" + "\'" + cur_user.userID + "\'" + ", \'" +search_val + "\');";

        System.out.println("Query: " + query);

        viewed_friends_names.clear();
        viewed_friends_id.clear();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);


        while(resultSet.next())
        {
            String user_name = resultSet.getString("user_name");
            String user_id = resultSet.getString("friend_ids");

            if(!viewed_friends_names.contains(user_name))
            {
                viewed_friends_names.add(user_name);
            }
            if(!viewed_list_id.contains(user_id))
            {
                viewed_friends_id.add(user_id);
            }


        }
    }

    public static ResultSet execute_query(String query)
            throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

}
