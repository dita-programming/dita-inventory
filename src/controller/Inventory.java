package controller;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ItemsModel;
import model.TeamsModel;
import view.MainView;
/**
 *
 * @author michael
 */
public class Inventory {
    // Choose the look and feel of the program
    static {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    static MainView main = new MainView();
    static ItemsModel itemsModel = new ItemsModel();
    static TeamsModel teamsModel = new TeamsModel();
    static DefaultTableModel tableModel = (DefaultTableModel) main.getCheckout().getModel();
    static DefaultTableModel stockListModel = (DefaultTableModel) main.getStockList().getModel();
    
    static AddHandler Add = new AddHandler();
    static RemoveHandler Remove = new RemoveHandler();
    static ReturnHandler Return = new ReturnHandler();
    static IssueHandler Issue = new IssueHandler();
    static ManageHandler Manage = new ManageHandler();
    static AdminHandler Admin = new AdminHandler();
    
    static ArrayList<Object[]> tempList = new ArrayList<>();
    static Map itemsList = new HashMap();
    
    //Handler for the Add Button in MainView
    static class AddHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           int quantity = (int) main.getItemCount().getValue();
           String item = main.getItem().getSelectedItem().toString();
           
           if(main.getTxtName().getText().isEmpty())
           {
               JOptionPane.showMessageDialog(main,"Please enter the name!!",
                                                  "Blank Name", 2);
               return;
           }
           
           // Checks to make that the quantity of an item added to the temporary
           // table should not be greater than current quantity in the stocklist
           // table
           if(main.getCbxOperation().getSelectedItem().equals("Issue") && quantity > Integer.parseInt(itemsModel.getItem(item).get(2)))
           {
               JOptionPane.showMessageDialog(main,"Quantity cannot be greater "+
                                                  "than the current quantity!!",
                                                  "Out of bounds", 2);
               return;
           }
           
           // Checks to make sure the items added to the temporary table 
           // are not greater than the current quantity in the stocklist table
           if(main.getCbxOperation().getSelectedItem().equals("Issue") && quantity > (int) itemsList.get(item))
           {
               JOptionPane.showMessageDialog(main,item+" left = "+
                                                  itemsList.get(item).toString(),
                                                  "Out of bounds", 2);
               return;
           }
           
           Object[] items = {main.getTeam().getSelectedItem().toString(),
                             main.getItem().getSelectedItem().toString(),
                             main.getTxtName().getText(),
                             main.getItemCount().getValue().toString()};
           
           tableModel.addRow(items);
           tempList.add(items);
           itemsList.replace(item, (int) itemsList.get(item)-quantity);
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
    
    //Handler for the Return Button in MainView
    static class ReturnHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           for(Object[] x:tempList)
            {
                String item = x[1].toString();
                String name = x[2].toString();
                int quantity = Integer.parseInt(x[3].toString());
                
                
                itemsModel.setCurrentQuantity(item, quantity, "add");
                itemsModel.logItemIn(item, LocalDateTime.now(), name, quantity);
            }
            tempList.clear();
            tableModel.setRowCount(0);
            Inventory.updateStockList();
            System.out.println("This ReturnHandler works");
       }
    }
    
    //Handler for the Issue Button in MainView
    static class IssueHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
            for(Object[] x:tempList)
            {
                String item = x[1].toString();
                String name = x[2].toString();
                int quantity = Integer.parseInt(x[3].toString());
                
                
                itemsModel.setCurrentQuantity(item, quantity, "subtract");
                itemsModel.logItemOut(item, LocalDateTime.now(), name, quantity);
            }
            tempList.clear();
            tableModel.setRowCount(0);
            Inventory.updateStockList();
           
       }
    }
    
    
     //Handler for the AddNew Button in MainView
    static class ManageHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           if(main.getCbxManage().getSelectedItem().toString().equals("Items"))
               IItems.show();
           else
               ITeams.show();
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
        main.getReturn().addActionListener(Return);
        main.getIssue().addActionListener(Issue);
        main.getManage().addActionListener(Manage);
        main.getAdmin().addActionListener(Admin);
        main.setLocationRelativeTo(null);
        updateStockList();
        updateItemComboBox();
        updateTeamComboBox();
        main.setVisible(true);
    }
    
    public static void updateStockList() {
        ArrayList<ArrayList<String>> stock;
        stock = itemsModel.getItems();
        
        if(stockListModel.getRowCount() != 0)
        {
            stockListModel.setRowCount(0);
            itemsList.clear();
        }
        
        for(ArrayList<String> x:stock) {
            Object[] items = {x.get(0), x.get(1), x.get(2)};
            stockListModel.addRow(items);
            itemsList.put(x.get(0), Integer.parseInt(x.get(2)));
        }   
    }
    
    public static void updateItemComboBox() {
        ArrayList<ArrayList<String>> stock;
        stock = itemsModel.getItems();
        
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
        main.getReturn().setEnabled(true);
        main.getIssue().setEnabled(true);
        main.getManage().setEnabled(true);
        main.getCbxManage().setEnabled(true);
        main.getLog().setEnabled(true);
        main.getAdmin().setEnabled(false);
    }
    
}
