/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import view.LogView;
import model.Logmodel;

/**
 *
 * @author MD
 */
public class LogController 
{
   static LogView logView = new LogView();
   static Logmodel logModel = new Logmodel();
   static DefaultTableModel logTableModel = (DefaultTableModel) logView.getLog().getModel();
   static Map itemsList = new HashMap();

   

    public static void show()
    {
        logView.setLocationRelativeTo(null);
        logView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        logView.setVisible(true);
    }
    public static void updateLogList()
    {
      {
        ArrayList<ArrayList<String>> Log;
        Log = logModel.getLog();

        if(logTableModel.getRowCount() != 0)
        {
            logTableModel.setRowCount(0);
            itemsList.clear();
        }

        for(ArrayList<String> x : Log)
        {
            Object[] items =
            {
                x.get(0), x.get(1), x.get(2), x.get(3), x.get(4), x.get(5), x.get(6)
            };
            logTableModel.addRow(items);
            itemsList.put(x.get(0), Integer.parseInt(x.get(2)));
        }
      }
    }
    
    
}