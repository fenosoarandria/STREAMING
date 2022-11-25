package main;
import serveur.*;
public class Main{
    public static void main(String[] args)throws Exception, ClassNotFoundException{
        try{
            Server serveur=new Server();
            serveur.getServeur();
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }
}