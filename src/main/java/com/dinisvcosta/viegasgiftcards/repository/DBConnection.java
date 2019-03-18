package com.dinisvcosta.viegasgiftcards.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection
{
    private static DBConnection instance = null;
    private DBConnection() { }
    private static Connection conn = null;

    public static DBConnection getInstance()
    {
        if(instance == null)
        {
            //creates instance of DBConnection
            instance = new DBConnection();

            //creates database connection
            try
            {
                Properties dbProps = new Properties();
                InputStream input = new FileInputStream("config.properties");

                dbProps.load(input);

                conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/" +
                        "?user=" + dbProps.get("MYSQL_USER") +
                        "&password=" +
                        dbProps.getProperty("MYSQL_PASSWORD"));

                PreparedStatement createDatabaseStatement = conn.prepareStatement("CREATE DATABASE viegasgiftcards");
                createDatabaseStatement.executeQuery();

            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());

            }

            //creates tables if they dont exist already
            try
            {
                Properties dbProps = new Properties();
                InputStream input = new FileInputStream("config.properties");

                dbProps.load(input);

                conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/viegasgiftcards" +
                        "?user=" + dbProps.get("MYSQL_USER") +
                        "&password=" +
                        dbProps.getProperty("MYSQL_PASSWORD"));

                String sqlCreateCodesTable = "CREATE TABLE IF NOT EXISTS codes (code VARCHAR(9), formattedCode VARCHAR(11), creationDate DATE, expirationDate DATE, isUsed TINYINT(1) DEFAULT \"0\");";

                PreparedStatement createCodesTableStatement = conn.prepareStatement(sqlCreateCodesTable);
                createCodesTableStatement.executeQuery();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());

            }
        }
        return instance;
    }

    public Connection getConnection()
    {
        return conn;
    }
}
