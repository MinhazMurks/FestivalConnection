package sample;

import java.sql.*;
import java.util.ArrayList;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {





    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));

        primaryStage.setScene(new Scene(root,1280, 800));
        primaryStage.getIcons().add(new Image("file:resources/test-image.png"));
        primaryStage.setTitle("Festival Connection");
        primaryStage.show();
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
            if(table.charAt(i) == ')')
            {
                continue;
            }
            else if(table.charAt(i) == '(')
            {
                first_paranthesis_index = i;
                break;
            }
        }

        String result = table.substring(0, first_paranthesis_index);
        return result;
    }
    public static ResultSet select_from_table(Statement statement, ArrayList<String> columns, String table) throws SQLException
    {

        String new_table = table;

        if(table.endsWith(")"))
        {
            new_table = remove_defs(table);
        }


        String sql = "SELECT ";
        int i;

        for(i = 0; i < columns.size(); i++)
        {
            sql = sql.concat(columns.get(i));

            if(i + 1 != columns.size())
            {
                sql = sql.concat(", ");
            }
        }

        sql = sql.concat("\nFROM " + new_table + ";");

        System.out.println("Query: " + sql);
        return statement.executeQuery(sql);
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

        sql.concat("\nFROM " + new_table + ";");

        System.out.println("Query: " + sql);
        return statement.executeQuery(sql);
    }



    public static void main(String[] args) {

        try(
                Connection connection = DriverManager.getConnection
                        (
                                "jdbc:mysql://festivalproject.mysql.database.azure.com/festivals_project",
                                "festival_admin@festivalproject",
                                "muK43her"
                        );

                Statement statement = connection.createStatement();)
        {

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


        launch(args);
    }
}
