package Interface;

import javax.swing.JFrame;

public class Window extends JFrame{

    //constructeur
    public Window(){
        this.setTitle("ChatSystem");
        //this.setSize(1200,800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(false); //utile si on veut que l'utilisateur utilise le bouton déconnexion pour sortir ?
        this.setResizable(false);
        this.setVisible(false);
    }

}