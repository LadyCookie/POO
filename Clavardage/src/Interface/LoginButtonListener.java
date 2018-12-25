package Interface;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import model.Controller;


public class LoginButtonListener implements ActionListener {
    private String login;
    private JTextField loginChooser;
    private Window loginFenetre;
    private Window chatFenetre;
    private JOptionPane errorLoginDialog;
    private ButtonGroup connexionButtonGroup;

    private ArrayList<String> existingLogin; // TODO liste des pseudos d�j� utilis�s


    public LoginButtonListener(JTextField logChooser, ButtonGroup bg, Window openfen, Window closedfen) {
        this.existingLogin = new ArrayList();
        this.existingLogin.add("Alice1");  // TODO g�rer majuscules/minuscules ?
        this.login = "";
        this.loginChooser = logChooser;
        this.chatFenetre = openfen;
        this.loginFenetre = closedfen;
        this.errorLoginDialog = new JOptionPane();
        this.connexionButtonGroup = bg;
    }


    //Action du bouton "Entrer" en fonction de la valeur entr�e par l'utilisateur
    public void actionPerformed(ActionEvent event) {
    	
        login = loginChooser.getText();
        if (login.length() < 1) {
            errorLoginDialog.showMessageDialog(null, "Vous avez oubli� d'entrer un pseudo !", "Erreur", JOptionPane.ERROR_MESSAGE);
        } else if (login.length() > 15) {
            errorLoginDialog.showMessageDialog(null, "Votre pseudo ne doit pas faire plus de 15 caract�res !", "Erreur", JOptionPane.ERROR_MESSAGE);
        } else if (login.contains(" ")==true){
        	errorLoginDialog.showMessageDialog(null, "Votre pseudo ne peut pas contenir d'espaces", "Erreur", JOptionPane.ERROR_MESSAGE);
        } else if (existingLogin.contains(login)) {
            errorLoginDialog.showMessageDialog(null, "Ce pseudo est d�j� utilis�", "Erreur", JOptionPane.ERROR_MESSAGE);
        } else if (connexionButtonGroup.getSelection()==null){
            errorLoginDialog.showMessageDialog(null, "Vous devez choisir un mode de connexion", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        else {            
            
            loginFenetre.setVisible(false);
            chatFenetre.setVisible(true);

        }

    }
}
