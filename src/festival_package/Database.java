package festival_package;


import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.UUID.*;



public class Database {




    //Globals
    public static ArrayList<Festival> Festivals = new ArrayList<>();
    public static ArrayList<String> Festivals_Names = new ArrayList<>();

    public static ArrayList<User> Users = new ArrayList<>();
    public static ArrayList<String> User_Names = new ArrayList<>();


    public static ArrayList<User> Displayed_Users = new ArrayList<>();
    public static ArrayList<String> Displayed_User_Names = new ArrayList<>();


    public static ArrayList<String> Locations = new ArrayList<>();

    public static ArrayList<String> test_values = new ArrayList<>();

    public static ArrayList<String> viewed_list = new ArrayList<>();

    public static String cur_user_name = "";
    public static String cur_user_guid = "";

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
    public static String authenticate(String username, String password) throws SQLException
    {
        String query = ("SELECT userID " +
                "FROM Users " +
                "WHERE user_name = '" + username + "';");
        System.out.println(query);

        String guid = "";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            guid = resultSet.getString("userID");
            System.out.println(guid);
        }

        return guid;
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
    public static void insertNewUser(String username, String password, String dob, String state, String city, String address, int zip) throws SQLException{
        String guid = UUID.randomUUID().toString();
        String insertUserSQL = ("INSERT INTO Users VALUES ('" + guid + "', '" + username + "', '" + dob + "', " + "0);");
        String insertLocationSQL = ("INSERT INTO Location VALUES ('" + guid + "', NULL, '"  +  city + "', '" + state + "', '" + address + "', " + zip + ");");

        System.out.println(insertUserSQL);
        System.out.println(insertLocationSQL);

        Statement statement = connection.createStatement();
        statement.executeUpdate(insertUserSQL);
        statement.executeUpdate(insertLocationSQL);
    }

    /**
     * Takes the current user_guid and queries the Location table for it.
     * TODO: ADD CONDITION CHECKS FOR INCORRECT OR NONEXISTENT GUID
     * @return The concatenated city and state columns corresponding with current guid
     */
    public static String getUserLocation() throws SQLException{
        String query = ("SELECT city, state " +
                "FROM Location " +
                "WHERE userID = '" + cur_user_guid + "';");
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
    public static String getFestivalLocation(String guid) throws SQLException{
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

        User temp = new User(username);

        result = Users.get(Users.indexOf(temp));

        return result;
    }

    public static void refresh_lists() throws SQLException, ParseException
    {
        refresh_users();
        refresh_festivals();
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

    public static void refresh_festivals() throws SQLException {
        Festivals.clear();

        ArrayList<String> columns = new ArrayList<>();

        columns.add("festID");
        columns.add("festival.name");
        columns.add("location");
        columns.add("production_comp");
        columns.add("type_fest");
        columns.add("start_date");
        columns.add("end_date");
        columns.add("price");
        String festival_table = "festival";

        columns.add("type_fest");

        //all the subtables of festival
        String art_table = "art";
        String beer_table = "beer";
        String comedy_table = "comedy";
        String music_table = "music";


        columns.add("providers.name as providers");
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

        query += "\n JOIN " + art_table + " ON " + festival_table + ".festID = " + art_table + ".festID";
        query += "\n JOIN " + beer_table + " ON " + festival_table + ".festID = " + beer_table + ".festID";
        query += "\n JOIN " + comedy_table + " ON " + festival_table + ".festID = " + comedy_table + ".festID";
        query += "\n JOIN " + music_table + " ON " + festival_table + ".festID = " + music_table + ".festID";
        query += "\n JOIN " + providers_table + " ON " + festival_table + ".festID = " + providers_table + ".festID";

        query += ";";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while(resultSet.next())
        {
            LocalDate start_date = date_from_string(resultSet.getDate("start_date").toString());
            LocalDate end_date = date_from_string(resultSet.getDate("end_date").toString());

            Festival temp = new Festival(resultSet.getString("fest_ID"),
                                         resultSet.getString("festival.name"),
                                         resultSet.getString("location"),
                                         resultSet.getString("production_comp"),
                                         resultSet.getString("fest_type"),
                                         start_date, end_date,
                                         resultSet.getFloat("price")
                    );


            if(Festivals.contains(temp))
            {
                Festivals.get(Festivals.indexOf(temp)).providers.add(resultSet.getString("name"));
                continue;
            }
            else if (temp.type.equals("Music"))
            {
                temp.genre = resultSet.getString("genre");
                temp.outdoor = resultSet.getBoolean("outdoor");
                temp.camping = resultSet.getBoolean("camping");
                temp.providers.add(resultSet.getString("name"));
            }
            else if (temp.type.equals("Comedy"))
            {
                temp.providers.add(resultSet.getString("name"));
            }
            else if (temp.type.equals("Art"))
            {
                temp.genre = resultSet.getString("genre");
                temp.providers.add(resultSet.getString("name"));
            }
            else if(temp.type.equals("Beer"))
            {
                temp.providers.add(resultSet.getString("name"));
            }

            Festivals.add(temp);
        }
        re_add_fest_names();

    }

    public static void re_add_fest_names()
    {
        Festivals_Names.clear();

        for(int i = 0; i < Festivals.size(); i++)
        {
            Festivals_Names.add(Festivals.get(i).name);
        }
    }

    public static void set_viewed_list(ArrayList<String> new_list)
    {
        viewed_list.clear();

        for(int i = 0; i < new_list.size(); i++)
        {
            viewed_list.add(new_list.get(i));
        }
    }



}
