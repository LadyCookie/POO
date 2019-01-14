package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.User;
import model.Controller;

public class InterfaceController implements PropertyChangeListener{

	static String SelectedContact;
	static Controller Cont = new Controller();
	static LoginWindow mafenetre;
    static ChatWindow fenetre2;
	
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
		////System.out.println("Controller : un Listener a été ajouté");
		pcs.addPropertyChangeListener(listener);
	}
    
    public void propertyChange(PropertyChangeEvent evt) {
		//si on a changé la liste d'utilisateur/session on met à jour la notre
		if(evt.getPropertyName().equals("userList")) {
			fenetre2.UpdateConnectedUsers(Cont.getModelData().usersConnected(),Cont.getModelData().getLocalUser().getUser().getUsername());
			//System.out.println("ControllerInterface: la liste d'utilisateurs a changé");
		} else if(evt.getPropertyName().equals("sessionList")) {
			fenetre2.UpdateHistorique(Cont.getModelData().getHistoric(SelectedContact));
			//System.out.println("ControllerInterface: la liste de session a changé");
		} else if(evt.getPropertyName().equals("activesessionList")) {
			//this.activesessionList = ((ArrayList<InetAddress>) evt.getNewValue());
		} 
	}
    
    public InterfaceController() {
    	SelectedContact="";
    	mafenetre = new LoginWindow();
    	fenetre2 = new ChatWindow();
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
	            		fenetre2.UpdateConnectedUsers(Cont.getModelData().usersConnected(),Cont.getModelData().getLocalUser().getUser().getUsername());
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
            public void windowClosing(WindowEvent e){
                Cont.PerformDisconnect(4445, 4445);
                e.getWindow().dispose();
            }
        });
	    
	    //change la valeur du contact selectionné quand on clique sur la liste
	    fenetre2.chatContactList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
	    		SelectedContact = fenetre2.chatContactList.getSelectedValue();
	    	//	fenetre2.chatContactList.scroll
	    		if (SelectedContact==null) {
	    			SelectedContact="";
	    		}else if(SelectedContact.equals(Cont.getModelData().getLocalUser().getUser().getUsername()+" (Moi)")){
	    			SelectedContact = Cont.getModelData().getLocalUser().getUser().getUsername();
	    		}
	    		fenetre2.UpdateHistorique(Cont.getModelData().getHistoric(SelectedContact));
			}
	    
	    });
	    
	    //envoi le message
	    fenetre2.chatSendButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    		JOptionPane errorLoginDialog = new JOptionPane();
	    		String message = fenetre2.chatTypeTextArea.getText();
	    		fenetre2.chatTypeTextArea.setText(null);
	    		if(SelectedContact.equals("")){
	    			errorLoginDialog.showMessageDialog(null, "Veuillez selectionner un contact", "Erreur", JOptionPane.ERROR_MESSAGE);
	    	//	}else if(SelectedContact.equals(Cont.getModelData().getLocalUser().getUser().getUsername())) {
	    	//		errorLoginDialog.showMessageDialog(null, "Vous ne pouvez pas vous parlez à vous-même", "Erreur", JOptionPane.ERROR_MESSAGE);
	    		} else if(message.length()<1){
	    			errorLoginDialog.showMessageDialog(null, "Il faut écrire un message", "Erreur", JOptionPane.ERROR_MESSAGE);
	    		}else if(!Cont.sendMessage(SelectedContact, message, 2000)){
	    			errorLoginDialog.showMessageDialog(null, "Il faut selectionner un contact connecté", "Erreur", JOptionPane.ERROR_MESSAGE);
	    		} 
			}
	    
	    });
	    
	    //envoyer unfichier
	    fenetre2.chatFileButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    		JOptionPane errorLoginDialog = new JOptionPane();
	    		JFileChooser fileChooser = new JFileChooser();
	    	    
	    	    int result = fileChooser.showOpenDialog(fenetre2);
	    	    if (result == JFileChooser.APPROVE_OPTION) {
	    	        File selectedFile = fileChooser.getSelectedFile();
	    	        if(!Cont.sendFile(SelectedContact, selectedFile.getAbsolutePath(), 2000)){
		    			errorLoginDialog.showMessageDialog(null, "Il faut selectionner un contact connecté", "Erreur", JOptionPane.ERROR_MESSAGE);
		    		}
	    	    }
			}
	    
	    });
	    
	  //envoyer unfichier
	    fenetre2.ChangePseudoButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    		JOptionPane errorLoginDialog = new JOptionPane();
	    	    String new_pseudo = fenetre2.changePseudoArea.getText();
	    	    fenetre2.changePseudoArea.setText(null);
	    	    if (new_pseudo.length() < 1) {
	                errorLoginDialog.showMessageDialog(null, "Vous avez oublié d'entrer un pseudo !", "Erreur", JOptionPane.ERROR_MESSAGE);
	            } else if (new_pseudo.length() > 15) {
	                errorLoginDialog.showMessageDialog(null, "Votre pseudo ne doit pas faire plus de 15 caractères !", "Erreur", JOptionPane.ERROR_MESSAGE);
	            } else if (new_pseudo.contains(" ")==true){
	            	errorLoginDialog.showMessageDialog(null, "Votre pseudo ne peut pas contenir d'espaces", "Erreur", JOptionPane.ERROR_MESSAGE);
	            }else if (new_pseudo.equals("ListRQ") || new_pseudo.equals("end") || new_pseudo.equals("disconnect")){
	            	errorLoginDialog.showMessageDialog(null, "Les logins : ListRQ, end, disconnect sont interdits", "Erreur", JOptionPane.ERROR_MESSAGE);
	            }else {  
	            	if(!Cont.ChangePseudo(new_pseudo)) {
	            		errorLoginDialog.showMessageDialog(null, "Ce pseudo n'est pas disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
	            	}	            	
	            }
			}
	    
	    });
	    
	    
    }
    
    
    
}
