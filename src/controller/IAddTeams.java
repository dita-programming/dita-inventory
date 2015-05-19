/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

//import static controller.IAddItems.itemsModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.TeamsModel;
import view.AddTeams;

public class IAddTeams {
    
    static TeamsModel teamsModel = new TeamsModel();
    static AddTeams addNew;
    static AddHandler Add = new AddHandler();
    static UpdateHandler Update = new UpdateHandler();
    static CancelHandler Cancel = new CancelHandler();
    static CheckBoxHandler Checkbox = new CheckBoxHandler();
    
    static class AddHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           String name = addNew.getTxtName().getText();
           
           if(name.isEmpty()) {
               JOptionPane.showMessageDialog(addNew,"Please enter the Team name",
                                                     "Empty Name", 2);
               return;
           }
           if(teamsModel.checkTeamExists(name)) {
               JOptionPane.showMessageDialog(addNew,"Team exists!!",
                                                     "Duplicate name", 2);
               addNew.getTxtName().setText("");
               return;
           }
           
           
           teamsModel.addTeam(name);
           addNew.dispose();
           Inventory.updateTeamComboBox();
           JOptionPane.showMessageDialog(addNew,"Team successfully added",
                                                     "Success", 2);
       }
    }
    
    static class UpdateHandler implements ActionListener
    {
       @Override
       public void actionPerformed(ActionEvent event) 
       {
           String name = addNew.getTxtName().getText();
           String newName = null;
           
           if(name.isEmpty()) {
               JOptionPane.showMessageDialog(addNew,"Please enter the Team name",
                                                     "Empty Name", 2);
               return;
               }
           if(!teamsModel.checkTeamExists(name)) {
               JOptionPane.showMessageDialog(addNew,"Team does not exist!!",
                                                     "Team not found", 2);
               addNew.getTxtName().setText("");
               return;   
           }
           if (addNew.getCbxUpdate().isSelected())
           {
               newName = addNew.getTxtUpdate().getText();
               
               if(newName.isEmpty()) 
               {
                JOptionPane.showMessageDialog(addNew,"Please enter the Team name",
                                                     "Empty Name", 2);
               }
           }
               
           else
           {
               JOptionPane.showMessageDialog(addNew, "please Enable the Checkbox","not Checked",2);
           }
           
           
         
           teamsModel.updateTeam(newName, name);
           addNew.dispose();
           Inventory.updateTeamComboBox();
           JOptionPane.showMessageDialog(addNew,"Team successfully updated",
                                                     "Success", 2);
           
       }
    }
    static class CheckBoxHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            if(addNew.getCbxUpdate().isSelected())
           {
                addNew.getLblUpdate().setEnabled(true);
                addNew.getTxtUpdate().setEnabled(true);  
           }else
            {
                addNew.getLblUpdate().setEnabled(false);
                addNew.getTxtUpdate().setEnabled(false); 
            }
            
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
    
    
    public static void show() 
    {     
        addNew = new AddTeams();
        
        addNew.getAdd().addActionListener(Add);
        addNew.getUpdate().addActionListener(Update);
        addNew.getCancel().addActionListener(Cancel);
        addNew.getCbxUpdate().addActionListener(Checkbox);
        
        addNew.setLocationRelativeTo(null);        
        addNew.setVisible(true);
    }
}
    

