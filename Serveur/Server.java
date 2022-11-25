package serveur;
import java.io.*;
import java.net.*;
import all.*;


public class Server {
    public void getServeur() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket=null;
        Socket s=null;
        try{
                System.out.println("Miandry connexion Client...");
                serverSocket = new ServerSocket(3000);
                s = serverSocket.accept();
                new MusicPlayer();
                System.out.println("Tafiditra");
        }catch(Exception e){
            System.out.println(e);
        }
    }
}