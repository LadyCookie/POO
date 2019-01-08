package Interface;

import model.Controller;

public class Test {
	
	public static void main (String arg[]){
	
		//init
	    LoginWindow mafenetre = new LoginWindow();
	    ChatWindow fenetre2 = new ChatWindow();
	    mafenetre.setVisible(true);
	    fenetre2.setVisible(false);
	    
	    
	    // bouton Entrer Login
	    Controller Cont = new Controller();
	    LoginButtonListener logListener = new LoginButtonListener(mafenetre.loginTextField, mafenetre.selectConnectButtonGroup, fenetre2, mafenetre);
	    mafenetre.loginButton.addActionListener(logListener);
	    
	    }


}
