/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;


public class AdminsModel extends Model {
    
    public AdminsModel() {
        super();
    }
    
    public Boolean addAdmin(String user, String password) {
        /*
         * Adds an item to the database
         */
        Boolean success = true;
        startConnection();
        String sql;
        try {
            sql = "INSERT INTO Admins(name,password) VALUES(?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,user);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        } catch(Exception e) {
            success = false;
            System.err.println("[model.addAdmin()]" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    public Boolean removeAdmin(String user) {
        Boolean success = true;
        startConnection();
        String sql;
        try {
            sql = "DELETE FROM Admins WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,user);
            preparedStatement.executeUpdate();
        } catch(Exception e) {
            success = false;
            System.err.println("[model.removeAdmin()]" +
                               e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    public ArrayList<String> getAdmin(String user) {
        ArrayList list = new ArrayList();
        startConnection();
        String sql;
        try {
            sql = "SELECT * FROM Admins WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,user);
            result = preparedStatement.executeQuery();
            if(result.next()) {
                list.add(result.getString("name"));
                list.add(result.getString("password"));
            }
        } catch(Exception e) {
           System.err.println("[model.getAdmin()]" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return list;
    }
    
}
