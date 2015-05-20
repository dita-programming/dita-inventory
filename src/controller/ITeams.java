/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.TeamsModel;
import view.Teams;

/**
 *
 * @author michael
 */
public class ITeams
{

    //static Inventory mainController;
    static TeamsModel teamsModel = new TeamsModel();
    static Teams teamsView;
    static SubmitHandler Submit = new SubmitHandler();
    static CancelHandler Cancel = new CancelHandler();
    static OperationHandler Operation = new OperationHandler();

    static class SubmitHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            String name = teamsView.getTxtName().getText().toLowerCase();
            String op = null;
            boolean opSuccess = false;

            if (name.isEmpty())
            {
                JOptionPane.showMessageDialog(teamsView, "Please enter the team name",
                        "Empty Name", 2);
                return;
            }

            switch (teamsView.getOperation().getSelectedItem().toString())
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
                teamsView.dispose();
                Inventory.updateTeamComboBox();
                JOptionPane.showMessageDialog(teamsView, "Team successfully " + op,
                        "Success", 2);
            }
        }

        public boolean addOperation(String name)
        {
            if (teamsModel.checkTeamExists(name))
            {
                JOptionPane.showMessageDialog(teamsView, "Team already exists!!",
                        "Team found", 2);
                teamsView.getTxtName().setText("");
                return false;
            }
            teamsModel.addTeam(name);
            return true;
        }

        public boolean updateOperation(String name)
        {
            String newName = null;
            if (!teamsModel.checkTeamExists(name))
            {
                JOptionPane.showMessageDialog(teamsView, "Team does not exist!!",
                        "Team not found", 2);
                teamsView.getTxtName().setText("");
                return false;
            }
            newName = teamsView.getTxtUpdate().getText().toLowerCase();
            if (newName.isEmpty())
            {
                JOptionPane.showMessageDialog(teamsView, "Please enter the new team name",
                        "Empty Name", 2);
                return false;
            }

            teamsModel.updateTeam(newName, name);
            return true;
        }

        public boolean removeOperation(String name)
        {
            if (!teamsModel.checkTeamExists(name))
            {
                JOptionPane.showMessageDialog(teamsView, "Team does not exist!!",
                        "Team not found", 2);
                teamsView.getTxtName().setText("");
                return false;
            }

            teamsModel.removeTeam(name);
            return true;
        }
    }

    static class CancelHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            //gets name from the text field
            teamsView.dispose();
        }
    }

    static class OperationHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            //gets name from the text field
            if (teamsView.getOperation().getSelectedItem().toString().equals("Update"))
            {
                teamsView.getTxtUpdate().setEnabled(true);
                teamsView.getLblUpdate().setEnabled(true);
            } else
            {
                teamsView.getTxtUpdate().setEnabled(false);
                teamsView.getLblUpdate().setEnabled(false);
            }
        }
    }

    public static void show()
    {
        teamsView = new Teams();

        teamsView.getSubmit().addActionListener(Submit);
        teamsView.getCancel().addActionListener(Cancel);
        teamsView.getOperation().addActionListener(Operation);

        teamsView.setLocationRelativeTo(null);
        teamsView.setVisible(true);
    }
}
