/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import view.Items;
import model.ItemsModel;

public class IItems
{

    //static Inventory mainController;
    static ItemsModel itemsModel = new ItemsModel();
    static Items itemsView;
    static SubmitHandler Submit = new SubmitHandler();
    static CancelHandler Cancel = new CancelHandler();
    static OperationHandler Operation = new OperationHandler();
    static NameHandler Name = new NameHandler();

    static class SubmitHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            String name = itemsView.getTxtName().getText().toLowerCase();
            String op = null;
            boolean opSuccess = false;

            if (name.isEmpty())
            {
                JOptionPane.showMessageDialog(itemsView, "Please enter the item name",
                        "Empty Name", 2);
                return;
            }

            switch (itemsView.getOperation().getSelectedItem().toString())
            {
                case "Add":
                    opSuccess = addOperation(name);
                    op = "added";
                    break;
                case "Update":
                    opSuccess = updateOperation(name);
                    op = "updated";
                    break;
                default:
                    opSuccess = removeOperation(name);
                    op = "removed";
            }

            if (opSuccess)
            {
                itemsView.dispose();
                Inventory.updateStockList();
                Inventory.updateItemComboBox();
                JOptionPane.showMessageDialog(itemsView, "Item successfully " + op,
                        "Success", 2);
            }
        }

        public boolean addOperation(String name)
        {
            if (itemsModel.checkItemExists(name))
            {
                JOptionPane.showMessageDialog(itemsView, "Item already exists!!",
                        "Item found", 2);
                itemsView.getTxtName().setText("");
                return false;
            }
            int quantity = (int) itemsView.getQuantity().getValue();
            itemsModel.addItem(name, quantity);
            return true;
        }

        public boolean updateOperation(String name)
        {
            String newName = null;
            int quantity, newQuantity;
            if (!itemsModel.checkItemExists(name))
            {
                JOptionPane.showMessageDialog(itemsView, "Item does not exist!!",
                        "Item not found", 2);
                itemsView.getTxtName().setText("");
                return false;
            }
            newName = itemsView.getTxtUpdate().getText().toLowerCase();
            if (newName.isEmpty())
            {
                JOptionPane.showMessageDialog(itemsView, "Please enter the new item name",
                        "Empty Name", 2);
                return false;
            }

            quantity = Integer.parseInt(itemsModel.getItem(name).get(1));
            newQuantity = (int) itemsView.getQuantity().getValue();

            itemsModel.setQuantity(name, newQuantity);
            if (quantity > newQuantity)
            {
                itemsModel.setCurrentQuantity(name, quantity - newQuantity, "subtract");
            } else if (newQuantity > quantity)
            {
                itemsModel.setCurrentQuantity(name, newQuantity - quantity, "add");
            }

            itemsModel.updateItem(newName, name);
            return true;
        }

        public boolean removeOperation(String name)
        {
            if (!itemsModel.checkItemExists(name))
            {
                JOptionPane.showMessageDialog(itemsView, "Item does not exist!!",
                        "Item not found", 2);
                itemsView.getTxtName().setText("");
                return false;
            }

            itemsModel.removeItem(name);
            return true;
        }
    }

    static class CancelHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            //gets name from the text field
            itemsView.dispose();
        }
    }

    static class OperationHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            //gets name from the text field

            switch (itemsView.getOperation().getSelectedItem().toString())
            {
                case "Add":
                    itemsView.getTxtUpdate().setEnabled(false);
                    itemsView.getLblUpdate().setEnabled(false);
                    itemsView.getQuantity().setEnabled(true);
                    break;
                case "Update":
                    itemsView.getTxtUpdate().setEnabled(true);
                    itemsView.getLblUpdate().setEnabled(true);
                    itemsView.getQuantity().setEnabled(true);
                    break;
                case "Remove":
                    itemsView.getQuantity().setEnabled(false);
                    itemsView.getTxtUpdate().setEnabled(false);
                    itemsView.getLblUpdate().setEnabled(false);
            }
        }
    }

    // This handler monitors the change text of the name textfield and fetches
    // the total quantity of an item if a match is found
    static class NameHandler implements DocumentListener
    {

        @Override
        public void changedUpdate(DocumentEvent event)
        {
            nameAnalyze();
        }

        @Override
        public void insertUpdate(DocumentEvent e)
        {
            nameAnalyze();
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            nameAnalyze();
        }

        public void nameAnalyze()
        {
            String name = itemsView.getTxtName().getText().toLowerCase();
            String op = itemsView.getOperation().getSelectedItem().toString();
            int quantity;

            if (itemsModel.checkItemExists(name) && op.equals("Update"))
            {
                quantity = Integer.parseInt(itemsModel.getItem(name).get(1));
                itemsView.getQuantity().setValue(quantity);
            }
            //System.out.println();
        }
    }

    public static void show()
    {
        itemsView = new Items();

        itemsView.getSubmit().addActionListener(Submit);
        itemsView.getOperation().addActionListener(Operation);
        itemsView.getCancel().addActionListener(Cancel);
        itemsView.getTxtName().getDocument().addDocumentListener(Name);

        itemsView.setLocationRelativeTo(null);
        itemsView.setVisible(true);
    }
}
