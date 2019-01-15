package Interface;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import data.MessageChat;
import data.Session;
import data.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ListIterator;


public class ChatWindow extends Window {
	private static final long serialVersionUID = 1L;
	/*
	Font normal =new Font(affichageHistorique.getFont().getName(),Font.PLAIN,affichageHistorique.getFont().getSize());
	Font italicsmallFont= new Font(affichageHistorique.getFont().getName(),Font.ITALIC,affichageHistorique.getFont().getSize()-2);
	Font boldFont= new Font(affichageHistorique.getFont().getName(),Font.BOLD,affichageHistorique.getFont().getSize());
	*/
	
    private JPanel chatWindowPanel; //panel global
    private JFrame frame ;
    
    protected JList<String> OnlineUserList;	//list de contacts
    protected JList<String> OfflineUserList;	//list de contacts
    
    protected JButton chatFileButton;	//bouton pour envoyer un fichier
    protected JButton chatSendButton; //bouton pour envoyer message
    protected JButton ChangePseudoButton; //bouton pour changer de pseudo
    //protected JScrollPane chatTypeScrollPane; 
    protected JTextField changePseudoArea;
    protected JTextField chatTypeTextArea;
    
    
    protected JTextArea affichageHistorique; 
    private JScrollPane HistoriqueScroll;
    private JScrollPane OnlineUserScroll;
    private JScrollPane OfflineUserScroll;
    
    public ChatWindow() {
        OnlineUserList.setModel(new DefaultListModel<String>());
        OfflineUserList.setModel(new DefaultListModel<String>());
        add(chatWindowPanel);
    }
    
    public void CreatePopup(String header, String message) {
           frame = new JFrame();
           frame.setSize(200,125);
           frame.setLayout(new GridBagLayout());
           GridBagConstraints constraints = new GridBagConstraints();
           constraints.gridx = 0;
           constraints.gridy = 0;
           constraints.weightx = 1.0f;
           constraints.weighty = 1.0f;
           constraints.insets = new Insets(5, 5, 5, 5);
           constraints.fill = GridBagConstraints.BOTH;
           JLabel headingLabel = new JLabel(header);
           headingLabel.setOpaque(false);
           frame.add(headingLabel, constraints);
           constraints.gridx++;
           constraints.weightx = 0f;
           constraints.weighty = 0f;
           constraints.fill = GridBagConstraints.NONE;
           constraints.anchor = GridBagConstraints.NORTH;
           constraints.gridx = 0;
           constraints.gridy++;
           constraints.weightx = 1.0f;
           constraints.weighty = 1.0f;
           constraints.insets = new Insets(5, 5, 5, 5);
           constraints.fill = GridBagConstraints.BOTH;
           JLabel messageLabel = new JLabel("<HtMl>"+message);
           frame.add(messageLabel, constraints);
           
           Timer timer = new Timer(2000, new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   frame.setVisible(false);
                       frame	.dispose();
                   }
               });
               timer.setRepeats(false);
               timer.start();
           
           frame.setVisible(true);
    }
        
    public void UpdateConnectedUsers (ArrayList<User> Onlinelist, String localpseudo,ArrayList<Session> listSession){
    	DefaultListModel<String> listModelOnline = new DefaultListModel<String>();
    	DefaultListModel<String> listModelOffline = new DefaultListModel<String>();
    	ArrayList<InetAddress> listOnlineAddr = new ArrayList<InetAddress>();
    	ListIterator<User> i= Onlinelist.listIterator();
 		while(i.hasNext()) {
 			User local=i.next();
 			listOnlineAddr.add(local.getAddr());
 			if(local.getUsername().equals(localpseudo)) {
 				listModelOnline.addElement(local.getUsername()+" (Moi)");
 			}else {
 				listModelOnline.addElement(local.getUsername());
 				
 			}
 			
 		}
        OnlineUserList.setModel(listModelOnline);
        OnlineUserScroll.setViewportView(OnlineUserList);
        JScrollBar vertical = OnlineUserScroll.getVerticalScrollBar();
 		vertical.setValue( vertical.getMinimum());
 		
 		ListIterator<Session> j= listSession.listIterator();
 		while(j.hasNext()) {
 			Session local=j.next();
 			if(!listOnlineAddr.contains(local.getOtherUserAddress())) {
 				listModelOffline.addElement(local.getOtherUserAddress().toString()); 			
 			}
 		}
 		OfflineUserList.setModel(listModelOffline);
        OfflineUserScroll.setViewportView(OfflineUserList);
        JScrollBar vertical2 = OfflineUserScroll.getVerticalScrollBar();
  		vertical2.setValue( vertical2.getMinimum());
    }
    
    public void UpdateHistorique(ArrayList<MessageChat> messageList){
    	affichageHistorique.setText("");
    	JTextArea n = new JTextArea();
        TitledBorder titleblanc = BorderFactory.createTitledBorder("Messages");
        titleblanc.setTitleColor(Color.WHITE);
        n.setBorder(titleblanc);
        n.setLineWrap(true);
        n.setWrapStyleWord(true);
        n.setEditable(false);
        n.setBackground(new Color(-12236470));
        n.setSelectedTextColor(Color.white);
        n.setForeground(Color.white);
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    	ListIterator<MessageChat> i= messageList.listIterator();
 		while(i.hasNext()) {
 			MessageChat local=i.next();
 		//affichageHistorique.append(local.getAuthor()+" ("+sdf.format(local.getDate())+") : "+local.getContent()+"\n");
 			n.append(local.getAuthor()+" ("+sdf.format(local.getDate())+") : "+local.getContent()+"\n");
 		}
 		System.out.println("J'ai recup les messages");
 		HistoriqueScroll.add(n);
 		HistoriqueScroll.setViewportView(n);
 		JScrollBar vertical = HistoriqueScroll.getVerticalScrollBar();
 		vertical.setValue( vertical.getMaximum() );
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
    	/*
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	int width = (int) (gd.getDisplayMode().getWidth() * 0.7);
    	int height = (int) (gd.getDisplayMode().getHeight() * 0.2);
    	*/
    	int width = 700;
    	int height =(int)(width * 0.7);
    	setSize(width,height);
    	setLocationRelativeTo(null);
    	
        chatWindowPanel = new JPanel();
        chatWindowPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
             
        //PANEL EN HAUT A GAUCHE
        TitledBorder title = BorderFactory.createTitledBorder("Online Users");
        title.setTitleColor(Color.GREEN);
        OnlineUserList = new JList<String>();
       // chatContactList.setPreferredSize(new Dimension((int)(width*0.4),(int)(height*0.57)));
        OnlineUserList.setBorder(title);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        OnlineUserScroll = new JScrollPane(OnlineUserList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        OnlineUserScroll.setPreferredSize(new Dimension((int)(width*0.4),(int)((height*0.57)/2)));
        chatWindowPanel.add(OnlineUserScroll, gbc);
        
      //PANEL EN HAUT A GAUCHE
        TitledBorder title2 = BorderFactory.createTitledBorder("Offline Users");
        title2.setTitleColor(Color.RED);
        OfflineUserList = new JList<String>();
       // chatContactList.setPreferredSize(new Dimension((int)(width*0.4),(int)(height*0.57)));
        OfflineUserList.setBorder(title2);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        OfflineUserScroll = new JScrollPane(OfflineUserList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        OfflineUserScroll.setPreferredSize(new Dimension((int)(width*0.4),(int)((height*0.57)/2)));
        chatWindowPanel.add(OfflineUserScroll, gbc);
            
        //PANEL EN BAS A GAUCHE
        changePseudoArea = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.ipadx =(int)(width*0.187);
        gbc.anchor = GridBagConstraints.NORTH;
      //  gbc.fill = GridBagConstraints.BOTH;
        chatWindowPanel.add(changePseudoArea, gbc);
        
        ChangePseudoButton = new JButton();
        ChangePseudoButton.setText("Change Login");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTH;
        //gbc.fill = GridBagConstraints.BOTH;
        chatWindowPanel.add(ChangePseudoButton, gbc);
        
        //PANEL EN HAUT A DROITE       
        affichageHistorique = new JTextArea();
        TitledBorder titleblanc = BorderFactory.createTitledBorder("Messages");
        titleblanc.setTitleColor(Color.WHITE);
        affichageHistorique.setBorder(titleblanc);
        affichageHistorique.setLineWrap(true);
        affichageHistorique.setWrapStyleWord(true);
        affichageHistorique.setEditable(false);
        affichageHistorique.setBackground(new Color(-12236470));
        affichageHistorique.setSelectedTextColor(Color.white);
        affichageHistorique.setForeground(Color.white);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        HistoriqueScroll = new JScrollPane(affichageHistorique,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        HistoriqueScroll.setPreferredSize(new Dimension((int)(width*0.4),(int)(height*0.57)));
        chatWindowPanel.add(HistoriqueScroll, gbc);
        
        chatTypeTextArea = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        chatWindowPanel.add(chatTypeTextArea, gbc);
        
        /*
        //PANEL EN BAS A DROITE
        chatSendButton = new JButton();
        chatSendButton.setText("Send");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
     //   gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_END;
        chatWindowPanel.add(chatSendButton, gbc); */
        
        chatFileButton = new JButton();
        chatFileButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
      //  gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        chatWindowPanel.add(chatFileButton, gbc);    
        
    }

    public JComponent $$$getRootComponent$$$() {
        return chatWindowPanel;
    }

}
