package festival_package;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

    public static ArrayList<Festival> Festivals = new ArrayList<>();
    public static ArrayList<User> Users = new ArrayList<>();




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
    public static ResultSet select_from_table(Statement statement, ArrayList<String> columns, String table) throws SQLException
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
        return statement.executeQuery(query);
    }
    public static ResultSet select_from_table(Statement statement, String column, String table) throws SQLException
    {

        String new_table = table;

        if(table.endsWith(")"))
        {
            new_table = remove_defs(table);
        }



        String sql = "SELECT " + column + " FROM " + new_table + ";";

        System.out.println("Query: " + sql);
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

    Database(){}


}
