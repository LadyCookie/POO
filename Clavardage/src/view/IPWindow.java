package view;

import javax.swing.*;
import java.awt.*;

public class IPWindow extends Window {
	
	private static final long serialVersionUID = 1L;
	protected JPanel loginPanel;
    protected JTextArea TextArea;
    protected JTextField IPField;
    public JButton connectButton;

    public IPWindow() {
        add(loginPanel);
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
    	this.setTitle("Distant Server");
    	setSize(200,200);
    	setLocationRelativeTo(null);
    	
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc;
        
        TextArea = new JTextArea();
        TextArea.setEditable(false);
        TextArea.setText("Enter the IP address of the distant server");
        //TextArea.setLineWrap(true);
        TextArea.setBackground(getBackground());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        loginPanel.add(TextArea, gbc);
        
        IPField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(IPField, gbc);
    
        connectButton = new JButton();
        connectButton.setText("Connect");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(connectButton, gbc);
    }

   
    public JComponent $$$getRootComponent$$$() {
        return loginPanel;
    }

}

