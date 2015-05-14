package controller;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.ItemsModel;
import model.TeamsModel;
import view.MainView;
/**
 *
 * @author michael
 */
public class Inventory {
    
    static MainView main = new MainView();
    static ItemsModel model = new ItemsModel();
    static TeamsModel teamsModel = new TeamsModel();
    static DefaultTableModel tableModel = (DefaultTableModel) main.getCheckout().getModel();
    static DefaultTableModel stockListModel = (DefaultTableModel) main.getStockList().getModel();
    
    static AddHandler Add = new AddHandler();
    static RemoveHandler Remove = new RemoveHandler();
    static AddItemHandler AddItem = new AddItemHandler();
    static AddTeamHandler AddTeam = new AddTeamHandler();
    static ReturnHandler Return = new ReturnHandler();
    static IssueHandler Issue = new IssueHandler();
    static AdminHandler Admin = new AdminHandler();
    
    //Handler for the Add Button in MainView
    static class AddHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           //gets name from the text field
           Object[] items = {main.getTeam().getSelectedItem().toString(),
                             main.getItem().getSelectedItem().toString(),
                             main.getName1().getText(),
                             main.getItemCount().getValue().toString()};
           
           tableModel.addRow(items);
       }
    }
    
    //Handler for the Add Button in MainView
    static class RemoveHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           int row = tableModel.getRowCount(); // Get total rows
           int selected_row = main.getCheckout().getSelectedRow(); // Get selected row if any
           if(row != 0)
               if(selected_row == -1)
                    tableModel.removeRow(row-1);
               else
                   tableModel.removeRow(selected_row);
       }
    }
    
    //Handler for the AddNew Button in MainView
    static class AddItemHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           IAddItems.show();
       }
    }
    
    static class AddTeamHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           IAddTeams.show();
       }
    }
    
    //Handler for the Return Button in MainView
    static class ReturnHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
            System.out.println("This ReturnHandler works");
       }
    }
    
    //Handler for the Issue Button in MainView
    static class IssueHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
            System.out.println("This IssueHandler works");
       }
    }
    
    static class AdminHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           AdminController.show();
       }
    }
    

    public static void main(String[] args) {
        //Connect event handlers to widgets
        main.getAdd().addActionListener(Add);
        main.getRemove().addActionListener(Remove);
        main.getAddItem().addActionListener(AddItem);
        main.getAddTeam().addActionListener(AddTeam);
        main.getReturn().addActionListener(Return);
        main.getIssue().addActionListener(Issue);
        main.getAdmin().addActionListener(Admin);
        main.setLocationRelativeTo(null);
        updateStockList();
        updateItemComboBox();
        updateTeamComboBox();
        main.setVisible(true);
    }
    
    public static void updateStockList() {
        ArrayList<ArrayList<String>> stock;
        stock = model.getItems();
        
        if(stockListModel.getRowCount() != 0)
            stockListModel.setRowCount(0);
        
        for(ArrayList<String> x:stock) {
            Object[] items = {x.get(0), x.get(1), x.get(2)};
            stockListModel.addRow(items);
        }   
    }
    
    public static void updateItemComboBox() {
        ArrayList<ArrayList<String>> stock;
        stock = model.getItems();
        
        if(main.getItem().getItemCount() != 0)
            main.getItem().removeAllItems();
        
        for(ArrayList<String> x:stock) {
            Object item = x.get(0);
            main.getItem().addItem(item);
        }
    }
    
    public static void updateTeamComboBox() {
        ArrayList<ArrayList<String>> stock;
        stock = teamsModel.getTeams();
        
        if(main.getTeam().getItemCount() != 0)
            main.getTeam().removeAllItems();
        
        for(ArrayList<String> x:stock) {
            Object team = x.get(0);
            main.getTeam().addItem(team);
        }
    }
    
    public static void enableButtons() {
        main.getAddItem().setEnabled(true);
        main.getAddTeam().setEnabled(true);
        main.getReturn().setEnabled(true);
        main.getIssue().setEnabled(true);
        main.getLog().setEnabled(true);
        main.getAdmin().setEnabled(false);
    }
    
}
