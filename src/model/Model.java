/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;

public abstract class Model
{

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/sports";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    protected Connection conn = null;
    protected Statement statement = null;
    protected PreparedStatement preparedStatement = null;
    protected ResultSet result = null;

    protected Model()
    {
        /*
         * Creates tables in the database if they don't exist
         */
        String sql;
        startConnection();
        try
        {
            statement = conn.createStatement();

            sql = "CREATE TABLE IF NOT EXISTS Teams"
                    + "(name VARCHAR(30) NOT NULL,"
                    + "PRIMARY KEY(name))";
            statement.addBatch(sql);

            sql = "CREATE TABLE IF NOT EXISTS Admins"
                    + "(name VARCHAR(30) NOT NULL,"
                    + "password VARCHAR(50) NOT NULL,"
                    + "PRIMARY KEY(name))";
            statement.addBatch(sql);

            sql = "CREATE TABLE IF NOT EXISTS Log" +
                    "(indx INTEGER NOT NULL AUTO_INCREMENT," +
                    "item VARCHAR(20) NOT NULL," +
                    "time_out DATETIME NOT NULL," +
                    "time_in DATETIME DEFAULT NULL," +
                    "name VARCHAR(30) NOT NULL," +
                    "issue_quantity INTEGER NOT NULL," +
                    "return_quantity INTEGER NOT NULL," +
                    "PRIMARY KEY(indx))";
            statement.addBatch(sql);

            statement.executeBatch();
        } catch (Exception e)
        {
            System.err.println("[model.Model()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
    }

    protected final Boolean startConnection()
    {
        /*
         * Creates connection to the database
         */
        Boolean success = true;
        try
        {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e)
        {
            success = false;
            System.err.println("[model.startConnnection()]" + e.getClass().getName() + ": " + e.getMessage());

        }
        return success;
    }

    protected final Boolean closeConnection()
    {
        /*
         * Closes connection to the database
         */
        Boolean success = true;
        try
        {
            if (result != null)
            {
                result.close();
            }

            if (statement != null)
            {
                statement.close();
            }

            if (conn != null)
            {
                conn.close();
            }
        } catch (Exception e)
        {
            success = false;
            System.err.println("[model.closeConnect()]" + e.getClass().getName() + ": " + e.getMessage());
        }

        return success;
    }
}
