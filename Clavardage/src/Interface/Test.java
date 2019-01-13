package Interface;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Session;
import data.User;
import model.Controller;

public class Test{
	
	public static void main (String arg[]){
		InterfaceController Controller = new InterfaceController();
		
		/*
	    Cont.addPropertyChangeListener(this);
	    mafenetre.setVisible(true);
	    fenetre2.setVisible(false);
	    
	    //on définit le listener du bouton
	    mafenetre.loginButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	        	
	    		JOptionPane errorLoginDialog = new JOptionPane();
	    		
	            String login = mafenetre.loginTextField.getText();
	            if (login.length() < 1) {
	                errorLoginDialog.showMessageDialog(null, "Vous avez oublié d'entrer un pseudo !", "Erreur", JOptionPane.ERROR_MESSAGE);
	            } else if (login.length() > 15) {
	                errorLoginDialog.showMessageDialog(null, "Votre pseudo ne doit pas faire plus de 15 caractères !", "Erreur", JOptionPane.ERROR_MESSAGE);
	            } else if (login.contains(" ")==true){
	            	errorLoginDialog.showMessageDialog(null, "Votre pseudo ne peut pas contenir d'espaces", "Erreur", JOptionPane.ERROR_MESSAGE);
	            }else if (login.equals("ListRQ") || login.equals("end") || login.equals("disconnect")){
	            	errorLoginDialog.showMessageDialog(null, "Les logins : ListRQ, end, disconnect sont interdits", "Erreur", JOptionPane.ERROR_MESSAGE);
	            }
	            else {  
	            	
	            	if(Cont.PerformConnect(login, 4445, 4445, 2000)) {
	            		fenetre2.UpdateConnectedUsers(Cont.getModelData().usersConnected());
		            	mafenetre.setVisible(false);
		            	fenetre2.setVisible(true);
	            	} else {
	            		errorLoginDialog.showMessageDialog(null, "Ce pseudo n'est pas disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
	            	}
	            }
	        }
	    
	    });
	    
	    //deconnection à la fermeture de la fenetre
	    fenetre2.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                Cont.PerformDisconnect(4445, 4445);
                e.getWindow().dispose();
            }
        });
	    
	    //change la valeur du contact selectionné quand on clique sur la liste
	    fenetre2.chatContactList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
	    		SelectedContact = fenetre2.chatContactList.getSelectedValue();
			}
	    
	    });
	    
	    //envoi le message
	    fenetre2.chatSendButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    		String message = fenetre2.chatTypeTextArea.getText();
	    		Cont.sendMessage(SelectedContact, message, 2000);
			}
	    
	    });     */
	    
	}
}
