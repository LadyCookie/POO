package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JOptionPane;

import model.Controller;

public class Test {
	
	public static void main (String arg[]){
	
		//init
	    LoginWindow mafenetre = new LoginWindow();
	    
	    mafenetre.setVisible(true);
	    
	    Controller Cont = new Controller();
	    
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
	            }
	            else {  
	            	
	            	if(Cont.PerformConnect("Constance", 4445, 4445, 2000)) {
	            		ChatWindow fenetre2 = new ChatWindow(Cont.getModelData().usersConnected());
		            	mafenetre.setVisible(false);
		            	fenetre2.setVisible(true);
	            	}
	            }
	        }
	    
	    });

	}
}
