/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ItemsModel extends Model
{

    public ItemsModel()
    {
        super();
    }

    public Boolean addItem(String item, int quantity)
    {
        /*
         * Adds an item to the database
         */
        Boolean success = true;
        startConnection();
        String sql;
        try
        {
            sql = "INSERT INTO Items(name,quantity) VALUES(?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, item);
            preparedStatement.setInt(2, quantity);
            preparedStatement.executeUpdate();
            setCurrentQuantity(item, quantity, "add");
        } catch (Exception e)
        {
            success = false;
            System.err.println("[model.addItem()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return success;
    }

    public ArrayList<String> getItem(String item)
    {
        /*
         *  Gets an item from the database
         */
        ArrayList list = new ArrayList();
        startConnection();
        String sql;
        try
        {
            sql = "SELECT * FROM Items WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, item);
            result = preparedStatement.executeQuery();
            if (result.next())
            {
                list.add(result.getString("name"));
                list.add(Integer.toString(result.getInt("quantity")));
                list.add(Integer.toString(result.getInt("current_quantity")));
            }
        } catch (Exception e)
        {
            System.err.println("[model.getItem()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return list;
    }

    public ArrayList<ArrayList<String>> getItems()
    {
        /*
         * Gets all items from the database
         */
        ArrayList list = new ArrayList();
        startConnection();
        String sql;
        try
        {
            sql = "SELECT * FROM Items";
            statement = conn.createStatement();
            result = statement.executeQuery(sql);

            while (result.next())
            {
                ArrayList temp = new ArrayList();
                temp.add(result.getString("name"));
                temp.add(Integer.toString(result.getInt("quantity")));
                temp.add(Integer.toString(result.getInt("current_quantity")));

                list.add(temp);
            }

        } catch (Exception e)
        {
            System.err.println("[model.getItems()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return list;
    }

    public Boolean removeItem(String item)
    {
        /*
         * Removes an item from the database
         */
        Boolean success = true;
        startConnection();
        String sql;
        try
        {
            sql = "DELETE FROM Items WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, item);
            preparedStatement.executeUpdate();
        } catch (Exception e)
        {
            success = false;
            System.err.println("[model.removeItem()]"
                    + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return success;
    }

    public Boolean updateItem(String newItem, String oldItem)
    {
        /*
         * Updates an item in the database
         */
        Boolean success = true;
        startConnection();
        String sql;
        try
        {
            sql = "UPDATE Items SET name=? WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newItem);
            preparedStatement.setString(2, oldItem);
            preparedStatement.executeUpdate();
        } catch (Exception e)
        {
            success = false;
            System.err.println("[model.updateItem()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return success;
    }

    public Boolean checkItemExists(String item)
    {
        /*
         * Checks if an item exists in the database
         */
        return !getItem(item).isEmpty();
    }

    public Boolean setQuantity(String item, int quantity)
    {
        /*
         * Sets the total quantity of an item in the database
         */
        Boolean success = true;
        startConnection();
        String sql;
        try
        {
            sql = "UPDATE Items SET quantity=? WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, item);
            preparedStatement.executeUpdate();
        } catch (Exception e)
        {
            success = false;
            System.err.println("[model.setQuantity()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return success;
    }

    public Boolean setCurrentQuantity(String item, int quantity, String op)
    {
        /*
         * Sets the current quantity of an item in the database
         */
        Boolean success = true;
        startConnection();
        String sql;
        try
        {
            if (op.equals("add"))
            {
                sql = "UPDATE Items SET current_quantity=current_quantity+? WHERE name=?";
            } else
            {
                sql = "UPDATE Items SET current_quantity=current_quantity-? WHERE name=?";
            }

            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, item);
            preparedStatement.executeUpdate();
        } catch (Exception e)
        {
            success = false;
            System.err.println("[model.setCurrentQuantity()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return success;
    }

    public Boolean logItemOut(String item, LocalDateTime datetime, String name, int quantity)
    {
        /*
         * Logs an item out into the database after an issue has occurred
         */
        Boolean success = true;
        startConnection();
        String sql;
        try
        {
            sql = "INSERT INTO Log(item,time_out,name,issue_quantity) VALUES(?,?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, item);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(datetime));
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, quantity);
            preparedStatement.executeUpdate();
        } catch (Exception e)
        {
            success = false;
            System.err.println("[model.logItemOut()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return success;
    }

    public Boolean logItemIn(String item, LocalDateTime datetime, String name, int quantity)
    {
        /*
         * Logs an item into the database after an issue has occurred
         */
        Boolean success = true;
        startConnection();
        String sql;
        try
        {
            sql = "UPDATE Log SET time_in=?, return_quantity=? "
                    + "WHERE (name = ?) AND (item = ?) AND (time_in IS NULL)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(datetime));
            preparedStatement.setInt(2, quantity);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, item);
            preparedStatement.executeUpdate();
        } catch (Exception e)
        {
            success = false;
            System.err.println("[model.logItemIn()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return success;
    }
}
