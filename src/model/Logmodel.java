package model;

import java.util.ArrayList;

/**
 *
 * @author MD
 */
public class Logmodel extends Model
{

    public Logmodel()
    {
        super();
    }

    public ArrayList<ArrayList<String>> getLog()
    {
        /*
         * Gets all items from the database
         */
        ArrayList list = new ArrayList();
        startConnection();
        String sql;
        try
        {
            sql = "SELECT * FROM Log";
            statement = conn.createStatement();
            result = statement.executeQuery(sql);

            while(result.next())
            {
                ArrayList temp = new ArrayList();
                temp.add(Integer.toString(result.getInt("indx")));
                temp.add(result.getString("item"));
                temp.add(result.getTimestamp("time_out").toLocalDateTime().toString());
                temp.add(result.getTimestamp("time_in").toLocalDateTime().toString());
                temp.add(result.getString("name"));
                temp.add(Integer.toString(result.getInt("issue_quantity")));
                temp.add(Integer.toString(result.getInt("return_quantity")));

                list.add(temp);
            }

        }
        catch(Exception e)
        {
            System.err.println("[model.getLog()]" + e.getClass().getName() + ": " + e.getMessage());
        }
        closeConnection();
        return list;
    }
}
