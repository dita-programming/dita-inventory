/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import java.util.ArrayList;

/**
 *
 * @author michael
 */
public class TeamsModel extends Model {
    
    public TeamsModel() {
        super();
    }
    
    public Boolean addTeam(String team) {
        /*
         * Adds a team to the database
         */
        Boolean success = true;
        startConnection();
        String sql;
        try {
            sql = "INSERT INTO Teams(name) VALUES(?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,team);
            preparedStatement.executeUpdate();
        } catch(Exception e) {
            success = false;
            System.err.println("[model.addTeam()]" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    public ArrayList<String> getTeam(String team) {
        /*
         *  Gets a team from the database
         */
        ArrayList list = new ArrayList();
        startConnection();
        String sql;
        try {
            sql = "SELECT * FROM Teams WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,team);
            result = preparedStatement.executeQuery();
            if(result.next()) {
                list.add(result.getString("name"));
            }
        } catch(Exception e) {
           System.err.println("[model.getTeam()]" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return list;
    }
    
    public ArrayList<ArrayList<String>> getTeams() {
        /*
         * Gets all teams from the database
         */
        ArrayList list = new ArrayList();
        startConnection();
        String sql;
        try {
            sql = "SELECT * FROM Teams";
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            
            while(result.next()) {
                ArrayList temp = new ArrayList();
                temp.add(result.getString("name"));
                
                list.add(temp);
            }
            
        } catch(Exception e) {
            System.err.println("[model.getTeams()]" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return list;
    }
    
    public Boolean removeTeam(String team) {
        /*
         * Removes a team from the database
         */
        Boolean success = true;
        startConnection();
        String sql;
        try {
            sql = "DELETE FROM Teams WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,team);
            preparedStatement.executeUpdate();
        } catch(Exception e) {
            success = false;
            System.err.println("[model.removeTeam()]" +
                               e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    public Boolean updateTeam(String newTeam, String oldTeam) {
        /*
         * Adds a team to the database
         */
        Boolean success = true;
        startConnection();
        String sql;
        try {
            sql = "UPDATE Teams SET name=? WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,newTeam);
            preparedStatement.setString(2,oldTeam);
            preparedStatement.executeUpdate();
        } catch(Exception e) {
            success = false;
            System.err.println("[model.updateTeam()]" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    public Boolean checkTeamExists(String team) {
        /*
         * Checks if a team exists in the database
         */
        return !getTeam(team).isEmpty();
    }
    
}
