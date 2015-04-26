/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import java.awt.event.*;
import javax.swing.JOptionPane;
import view.AddItems;
import model.ItemsModel;

/**
 *
 * @author michael
 */
public class IAddItems {
    static ItemsModel itemsModel = new ItemsModel();
    static AddItems addNew;
    static AddHandler Add = new AddHandler();
    static UpdateHandler Update = new UpdateHandler();
    static CancelHandler Cancel = new CancelHandler();
    
    static class AddHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           String name = addNew.getTxtName().getText();
           int quantity;
           
           if(name.isEmpty()) {
               JOptionPane.showMessageDialog(addNew,"Please enter the item name",
                                                     "Empty Name", 2);
               return;
           }
           
           if(addNew.getQuantity().getValue().toString().isEmpty()) {
               JOptionPane.showMessageDialog(addNew,"Please enter the item quantity",
                                                     "Empty Quantity", 2);
               return;
           }
           else {
               quantity = Integer.parseInt(addNew.getQuantity().getValue().toString());
           }
           
           if(itemsModel.checkItemExists(name)) {
               JOptionPane.showMessageDialog(addNew,"Item exists!!",
                                                     "Duplicate name", 2);
               addNew.getTxtName().setText("");
               return;
           }
           
           itemsModel.addItem(name, quantity);
           addNew.dispose();
           JOptionPane.showMessageDialog(addNew,"Item successfully added",
                                                     "Success", 2);
           
       }
    }
    
    static class UpdateHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           String name = addNew.getTxtName().getText();
           int quantity;
           
           if(name.isEmpty()) {
               JOptionPane.showMessageDialog(addNew,"Please enter the item name",
                                                     "Empty Name", 2);
               return;
           }
           
           if(addNew.getQuantity().getValue().toString().isEmpty()) {
               JOptionPane.showMessageDialog(addNew,"Please enter the item quantity",
                                                     "Empty Quantity", 2);
               return;
           }
           else {
               quantity = Integer.parseInt(addNew.getQuantity().getValue().toString());
           }
           
           if(!itemsModel.checkItemExists(name)) {
               JOptionPane.showMessageDialog(addNew,"Item does not exist!!",
                                                     "Item not found", 2);
               addNew.getTxtName().setText("");
               return;
           }
           
           itemsModel.setQuantity(name, quantity);
           addNew.dispose();
           JOptionPane.showMessageDialog(addNew,"Item successfully updated",
                                                     "Success", 2);
           
       }
    }
    
    static class CancelHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           //gets name from the text field
           addNew.dispose();
       }
    }
    
    
    public static void show() {     
        addNew = new AddItems();
        
        addNew.getAdd().addActionListener(Add);
        addNew.getUpdate().addActionListener(Update);
        addNew.getCancel().addActionListener(Cancel);
        
        addNew.setLocationRelativeTo(null);        
        addNew.setVisible(true);
    }
}
