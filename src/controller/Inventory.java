package controller;

import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import model.ItemsModel;
import model.TeamsModel;
import view.MainView;
import view.LogView;

public class Inventory
{

    // Choose the look and feel of the program
    static
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | 
                IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e)
        {
            System.err.println("[Inventory()]" + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    static MainView main = new MainView();
    static ItemsModel itemsModel = new ItemsModel();
    static TeamsModel teamsModel = new TeamsModel();
    
    static LogView log = new LogView();
    static DefaultTableModel tableModel = (DefaultTableModel) main.getCheckout().getModel();
    static DefaultTableModel stockListModel = (DefaultTableModel) main.getStockList().getModel();

    static AddHandler Add = new AddHandler();
    static RemoveHandler Remove = new RemoveHandler();
    static SubmitHandler Submit = new SubmitHandler();
    static ManageHandler Manage = new ManageHandler();
    static AdminHandler Admin = new AdminHandler();
    static LogHandler Log = new LogHandler();

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
            int itemQuantity, itemCurrentQuantity;

            if(main.getTxtName().getText().isEmpty())
            {
                JOptionPane.showMessageDialog(main, "Please enter the name!!",
                        "Blank Name", 2);
                return;
            }

            itemQuantity = Integer.parseInt(itemsModel.getItem(item).get(1));
            itemCurrentQuantity = Integer.parseInt(itemsModel.getItem(item).get(2));

            if(main.getCbxOperation().getSelectedItem().equals("Issue"))
            {
                // Checks to make sure that the quantity of an item issued to the temporary
                // table should not be greater than current quantity in the stocklist
                // table
                if(quantity > itemCurrentQuantity)
                {
                    JOptionPane.showMessageDialog(main, "Quantity issued cannot be greater "
                            + "than the current quantity!! "
                            + "(current quantity = "
                            + itemCurrentQuantity + ")",
                            "Out of bounds", 2);
                    return;
                }

                // Checks to make sure the items added to the temporary table 
                // are not greater than the current quantity in the stocklist table
                if(quantity > (int) itemsList.get(item))
                {
                    JOptionPane.showMessageDialog(main, item + " left = "
                            + itemsList.get(item).toString(),
                            "Out of bounds", 2);
                    return;
                }

                itemsList.replace(item, (int) itemsList.get(item) - quantity);

            }
            else
            {
                // Checks to make that the quantity of an item returned to the temporary
                // table should not be greater than total quantity in the stocklist
                // table
                if(quantity > itemQuantity)
                {
                    JOptionPane.showMessageDialog(main, "Quantity issued cannot be greater "
                            + "than the total quantity!! "
                            + "(total quantity = "
                            + itemQuantity + ")",
                            "Out of bounds", 2);
                    return;
                }

                // Checks to make that the quantity of an item returned to the temporary
                // table should not be greater than the slots available
                if(quantity > itemQuantity - (int) itemsList.get(item))
                {
                    JOptionPane.showMessageDialog(main, item + " to be returned = "
                            + (itemQuantity - (int) itemsList.get(item)),
                            "Out of bounds", 2);

                    return;
                }

                itemsList.replace(item, (int) itemsList.get(item) + quantity);
            }

            Object[] items =
            {
                main.getTeam().getSelectedItem().toString(),
                main.getItem().getSelectedItem().toString(),
                main.getTxtName().getText(),
                main.getItemCount().getValue().toString(),
                main.getCbxOperation().getSelectedItem().toString()
            };

            tableModel.addRow(items);
            tempList.add(items);
            main.getTxtName().setText(null);
        }
    }

    //Handler for the Remove Button in MainView
    static class RemoveHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            int row = tableModel.getRowCount(); // Get total rows
            int selected_row = main.getCheckout().getSelectedRow(); // Get selected row if any
            if(row != 0)
            {
                int quantity;
                String item, operation;
                if(selected_row == -1)
                {
                    item = tableModel.getValueAt(row - 1, 1).toString();
                    quantity = Integer.parseInt(tableModel.getValueAt(row - 1, 3).toString());
                    operation = tableModel.getValueAt(row - 1, 4).toString();
                    tableModel.removeRow(row - 1);
                    tempList.clear();
                }
                else
                {
                    item = tableModel.getValueAt(selected_row, 1).toString();
                    quantity = Integer.parseInt(tableModel.getValueAt(selected_row, 3).toString());
                    operation = tableModel.getValueAt(selected_row, 4).toString();
                    tableModel.removeRow(selected_row);
                    tempList.remove(selected_row);
                }

                if(operation.equals("Issue"))
                {
                    itemsList.replace(item, (int) itemsList.get(item) + quantity);
                }
                else
                {
                    itemsList.replace(item, (int) itemsList.get(item) - quantity);
                }
            }
        }
    }

    //Handler for the Issue Button in MainView
    static class SubmitHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            for(Object[] x : tempList)
            {
                String item = x[1].toString();
                String name = x[2].toString();
                int quantity = Integer.parseInt(x[3].toString());
                String operation = x[4].toString();

                if(operation.equals("Issue"))
                {
                    itemsModel.setCurrentQuantity(item, quantity, "subtract");
                    itemsModel.logItemOut(item, LocalDateTime.now(), name, quantity);
                }
                else
                {
                    itemsModel.setCurrentQuantity(item, quantity, "add");
                    itemsModel.logItemIn(item, LocalDateTime.now(), name, quantity);
                }

            }
            tempList.clear();
            tableModel.setRowCount(0);
            Inventory.updateStockList();
        }
    }

    //Handler for the Manage Button in MainView
    static class ManageHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            if(main.getCbxManage().getSelectedItem().toString().equals("Items"))
            {
                IItems.show();
            }
            else
            {
                ITeams.show();
            }
        }
    }

    //Handler for the Admin Button in MainView
    static class AdminHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            AdminController.show();
        }
    }
    //Handler for the Log Button in MainView
    static class LogHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            LogController.show();
        }
    }

    public static void main(String[] args)
    {
        //Connect event handlers to widgets
        main.getAdd().addActionListener(Add);
        main.getRemove().addActionListener(Remove);
        main.getSubmit().addActionListener(Submit);
        main.getManage().addActionListener(Manage);
        main.getAdmin().addActionListener(Admin);
        main.getLog().addActionListener(Log);
        main.setLocationRelativeTo(null);
        updateStockList();
        updateItemComboBox();
        updateTeamComboBox();
        main.setVisible(true);
    }

    public static void updateStockList()
    {
        ArrayList<ArrayList<String>> stock;
        stock = itemsModel.getItems();

        if(stockListModel.getRowCount() != 0)
        {
            stockListModel.setRowCount(0);
            itemsList.clear();
        }

        for(ArrayList<String> x : stock)
        {
            Object[] items =
            {
                x.get(0), x.get(1), x.get(2)
            };
            stockListModel.addRow(items);
            itemsList.put(x.get(0), Integer.parseInt(x.get(2)));
        }
    }

    public static void updateItemComboBox()
    {
        ArrayList<ArrayList<String>> stock;
        stock = itemsModel.getItems();

        if(main.getItem().getItemCount() != 0)
        {
            main.getItem().removeAllItems();
        }

        for(ArrayList<String> x : stock)
        {
            Object item = x.get(0);
            main.getItem().addItem(item);
        }
    }

    public static void updateTeamComboBox()
    {
        ArrayList<ArrayList<String>> stock;
        stock = teamsModel.getTeams();

        if(main.getTeam().getItemCount() != 0)
        {
            main.getTeam().removeAllItems();
        }

        for(ArrayList<String> x : stock)
        {
            Object team = x.get(0);
            main.getTeam().addItem(team);
        }
    }

    public static void enableButtons()
    {
        main.getSubmit().setEnabled(true);
        main.getManage().setEnabled(true);
        main.getCbxManage().setEnabled(true);
        main.getLog().setEnabled(true);
        main.getAdmin().setEnabled(false);
    }
}
