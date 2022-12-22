package listener;

import java.awt.event.*;

import client.Client;
public class Listener implements ActionListener {
    Client client;
    public void setListener(Client c){
        this.client=c;
    }
    public void actionPerformed (ActionEvent e){
        if(e.getSource()==client.getBoutton()[0]){
            try {
                client.getObjectOutput().writeObject(client.getSelected().getSelectedItem());
                client.getObjectOutput().flush();
                System.out.println("ok");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if(e.getSource()==client.getBoutton()[1]){
            try {
                client.getObjectOutput().writeObject(e.getActionCommand());
                client.PlayVideo();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
