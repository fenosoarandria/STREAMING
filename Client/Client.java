package client;
import java.io.*;
import all.*;
import java.net.*;
import javax.swing.*;
public class Client{
    
    public void getClient()throws IOException, ClassNotFoundException{
        Socket socket=null;
        try{
            System.out.println("Mbl ts tafidtra");
            socket = new Socket("192.168.10.93",8000);
            new MusicPlayer();

        }catch(Exception e){
            System.out.println(e);
        }
    }
}