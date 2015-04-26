
package controller;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author michael
 */
public class Model {
    private static String DRIVER = "com.mysql.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost/sports";
    private static String USER = "root";
    private static String PASSWORD = "root";
    private Connection conn = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet result = null;
    
    public Model() {
        createTables();
    }
    
    private Boolean createTables() {
        Boolean success = true;
        String sql;
        startConnection();
        try {
            statement = conn.createStatement();
      
            sql = "CREATE TABLE IF NOT EXISTS Teams" +
                    "(name VARCHAR(30) NOT NULL," +
                    "total_members INTEGER NOT NULL," +
                    "PRIMARY KEY(name))";
            statement.addBatch(sql);
            


            sql = "CREATE TABLE IF NOT EXISTS Members" +
                    "(name VARCHAR(30) NOT NULL," +
                    "team VARCHAR(30) NOT NULL," +
                    "password VARCHAR(50) NOT NULL," +
                    "PRIMARY KEY(name)," +
                    "FOREIGN KEY(team) REFERENCES Teams(name))";
            statement.addBatch(sql);

            sql = "CREATE TABLE IF NOT EXISTS Items" +
                    "(name VARCHAR(20) NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "current_quantity INTEGER NOT NULL DEFAULT 0," +
                    "PRIMARY KEY(name))";
            statement.addBatch(sql);

            sql = "CREATE TABLE IF NOT EXISTS Log" +
                    "(indx INTEGER NOT NULL," +
                    "item VARCHAR(20) NOT NULL," +
                    "time_out TIME NOT NULL," +
                    "time_in TIME DEFAULT NULL," +
                    "name VARCHAR(30) NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "PRIMARY KEY(indx)," +
                    "FOREIGN KEY(name) REFERENCES Members(name))";
            statement.addBatch(sql);

            statement.executeBatch();
        } catch(Exception e) {
            success = false;
            System.err.println("(createTables)" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    public Boolean addItem(String item, int quantity) {
        Boolean success = true;
        startConnection();
        String sql;
        try {
            sql = "INSERT INTO Items(name,quantity) VALUES(?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,item);
            preparedStatement.setInt(2, quantity);
            preparedStatement.executeUpdate();
            setCurrentQuantity(item,quantity,"add");
        } catch(Exception e) {
            success = false;
            System.err.println("(addItem)" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    public ArrayList<String> getItem(String item) {
        ArrayList list = new ArrayList();
        startConnection();
        String sql;
        try {
            sql = "SELECT * FROM Items WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,item);
            result = preparedStatement.executeQuery();
            if(result.next()) {
                list.add(result.getString("name"));
                list.add(Integer.toString(result.getInt("quantity")));
                list.add(Integer.toString(result.getInt("current_quantity")));
            }
        } catch(Exception e) {
           System.err.println("(getItem)" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return list;
    }
    
    public ArrayList<ArrayList<String>> getItems() {
        ArrayList list = new ArrayList();
        startConnection();
        String sql;
        try {
            sql = "SELECT * FROM Items";
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            
            while(result.next()) {
                ArrayList temp = new ArrayList();
                temp.add(result.getString("name"));
                temp.add(Integer.toString(result.getInt("quantity")));
                temp.add(Integer.toString(result.getInt("current_quantity")));
                
                list.add(temp);
            }
            
        } catch(Exception e) {
            System.err.println("(getItems)" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return list;
    }
    
    public Boolean removeItem(String item) {
        Boolean success = true;
        startConnection();
        String sql;
        try {
            sql = "DELETE FROM Items WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,item);
            preparedStatement.executeUpdate();
        } catch(Exception e) {
            success = false;
            System.err.println("(removeItem)" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    public Boolean checkItemExists(String item) {
        if(getItem(item).isEmpty())
            return false;
        else
            return true;
    }
    public Boolean setQuantity(String item, int quantity) {
        Boolean success = true;
        startConnection();
        String sql;
        try {
            sql = "UPDATE Items SET quantity=? WHERE name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, item);
            preparedStatement.executeUpdate();
        } catch(Exception e) {
            success = false;
            System.err.println("(setQuantity)" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    public Boolean setCurrentQuantity(String item, int quantity, String op) {
        Boolean success = true;
        startConnection();
        String sql;
        try {
            if(op.equals("add"))
                sql = "UPDATE Items SET current_quantity=current_quantity+? WHERE name=?";
            else
                sql = "UPDATE Items SET current_quantity=current_quantity-? WHERE name=?";
            
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, item);
            preparedStatement.executeUpdate();
        } catch(Exception e) {
            success = false;
            System.err.println("(setCurrentQuantity)" + e.getClass().getName() + ": " + e.getMessage());
        } 
        closeConnection();
        return success;
    }
    
    /*private Boolean addMember() {
        
    }
    
    private Boolean removeMember() {
        
    }
    
    private ArrayList<String> getMember() {
        
    }
    
    private Boolean checkMemberExists() {
        
    }*/
    
    private Boolean startConnection() {
        Boolean success = true;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
        } catch(Exception e) {
            success = false;
            System.err.println("(startConnnection)" + e.getClass().getName() + ": " + e.getMessage());
            
        } 
        return success;
    }
    
    private Boolean closeConnection() {
        Boolean success = true;
        try {
            if(result != null) {
                result.close();
            }

            if (statement != null) {
                statement.close();
            }

            if(conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            success = false;
            System.err.println("(closeConnect)" + e.getClass().getName() + ": " + e.getMessage());
        }
        
        return success;
    }
}
