package festival_package;


import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;



public class Database {




    //Globals
    public static ArrayList<Festival> Festivals = new ArrayList<>();

    public static ArrayList<User> Users = new ArrayList<>();
    public static ArrayList<String> User_Names = new ArrayList<>();

    public static ArrayList<String> Locations = new ArrayList<>();

    public static ArrayList<String> test_values = new ArrayList<>();

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
                query = query.concat(", ");
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

        sql = sql.concat("\nFROM " + new_table + ";");

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

    public static void refresh_lists()
    {

    }

    public static void refresh_users() throws SQLException, ParseException
    {
        Users.clear();

        ArrayList<String> columns = new ArrayList<>();

        columns.add("userId");
        columns.add("user_name");
        columns.add("birthdate");
        columns.add("user_location");
        columns.add("is_company");

        ResultSet rows_user_IDs = select_from_table( columns,"users");
        System.out.println("GOT TO RESULTSET");

        while(rows_user_IDs.next())
        {
            LocalDate birth_date = date_from_string(rows_user_IDs.getDate("birthdate").toString());

            User cur_user = new User(rows_user_IDs.getString("userID"),
                    rows_user_IDs.getString("user_name"),
                    birth_date,
                    rows_user_IDs.getString("user_location"),
                    rows_user_IDs.getBoolean("is_company"));

            Users.add(cur_user);
            System.out.println(cur_user.toString());
        }

        rows_user_IDs.close();
        re_add_user_names();


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

        columns.add("festID");
        columns.add("location");
        columns.add("production_comp");
        columns.add("type_fest");
        columns.add("start_date");
        columns.add("end_date");
        columns.add("price");

        columns.add("type_fest");

        columns.add("name");

    }




}
