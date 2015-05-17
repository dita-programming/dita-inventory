/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import java.awt.event.*;
import javax.swing.JOptionPane;
import view.Admin;
import model.AdminsModel;

public class AdminController {
    static Admin admin = new Admin(); 
    static AdminsModel model = new AdminsModel();
    static LoginHandler Login = new LoginHandler();
    static CancelHandler Cancel = new CancelHandler();
    
    static class LoginHandler implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event) {
           String name = admin.getUserName().getText();
           String password = admin.getPassword().getText();
           String password1;
           
           if(name.isEmpty()) {
               JOptionPane.showMessageDialog(admin,"Please enter the Username",
                                                     "Username", 2);
               return;
           }
           
           if(password.isEmpty()) {
               JOptionPane.showMessageDialog(admin,"Please enter the password",
                                                     "Password", 2);
               return;
           }
           
           if(!model.getAdmin(name).isEmpty()) {
                password1 = model.getAdmin(name).get(1);
                if(password.equals(password1)) {
                    Inventory.enableButtons();
                    admin.dispose();
                    JOptionPane.showMessageDialog(admin,"Login successful",
                                                     "Success", 2);
                }
                else {
                    JOptionPane.showMessageDialog(admin,"Invalid password",
                                                     "Password", 2);
                }    
           }
           else {
               
               JOptionPane.showMessageDialog(admin,"Admin does not exist",
                                                     "Admin error", 2);
           }
        }
    } 
    
    static class CancelHandler implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event) {
            admin.dispose();
        }
    }
    
    public static void show() {
        admin = new Admin();
        admin.getLogin().addActionListener(Login);
        admin.getCancel().addActionListener(Cancel);
        
        admin.setLocationRelativeTo(null);
        admin.setVisible(true);
    }
    
}
