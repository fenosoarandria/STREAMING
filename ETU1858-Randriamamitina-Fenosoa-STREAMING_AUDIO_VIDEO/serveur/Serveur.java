package serveur;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import java.lang.*;
import javax.imageio.ImageIO;

public class Serveur {

  DataOutputStream dataOutputStream = null;
  DataInputStream dataInputStream = null;

  Socket s=null;
  int port=4000;
  public Serveur()throws Exception{
    getServer();
  }

  public void getServer()throws Exception{
    ServerSocket servsock = new ServerSocket(port);

    Socket socket = servsock.accept();
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

    File file = new File("Autre\\stallone.JPG");
    out.writeUTF(file.getName().toLowerCase());

    File fichierMp3 = new File("Autre\\Alan Walker - 135.mp3");
    out.writeUTF(fichierMp3.getName().toLowerCase());

    File fichier = new File("Autre\\denise.mp4");
    dataOutputStream = new DataOutputStream(socket.getOutputStream());
    dataOutputStream.writeUTF(fichier.getName().toLowerCase());

    while(true) {
      ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
      String demande = (String) ois.readObject();
      System.out.println(demande);
      

      if(demande.contains(".jpg") || demande.contains(".png")) {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(demande);
        /* Envoyer Image */
        OutputStream outputStream=socket.getOutputStream();
        BufferedImage image=ImageIO.read(file);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ImageIO.write(image, "jpg",byteArrayOutputStream );
        byte[] size =ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();

        outputStream.write(size);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.flush();
        System.out.println("Sending image......");
        System.out.println("Flushed "+System.currentTimeMillis());

        Thread.sleep(120000);
        System.out.println("Closing "+System.currentTimeMillis());
        socket.close();
      }

      if(demande.contains(".mp3")) {

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(demande);

        FileInputStream inputStream = new FileInputStream(fichierMp3);
        byte[] mybytearray = inputStream.readAllBytes();

        
        while (true) {
          System.out.println("Connected");
          System.out.println(socket.getInetAddress());
          out.writeUTF(fichierMp3.getName().toLowerCase());
          out.write(mybytearray);
        }
      }

      if(demande.contains(".mp4")) {

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(demande);

        try {
          System.out.println("Connected");
          dataInputStream = new DataInputStream(socket.getInputStream());
          dataOutputStream = new DataOutputStream(socket.getOutputStream());

          int bytes = 0;
          FileInputStream fileInputStream = new FileInputStream(fichier);

          dataOutputStream.writeLong(fichier.length());

          byte[] buffer = new byte[4 * 1024];
          while ((bytes = fileInputStream.read(buffer)) != -1) {
          
          dataOutputStream.write(buffer, 0, bytes);
          dataOutputStream.flush();
          }
          fileInputStream.close();
          dataInputStream.close();
          dataOutputStream.close();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

  }
    
}

