package client;

import java.net.*;
import java.net.Socket;

import javax.sound.sampled.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import listener.Listener;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.*;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.awt.event.*;

import mp3Play.*;

public class Client{
    
    Listener listener=new Listener();
    JFrame[] frame = new JFrame[4];
    JButton[] button = new JButton[3];
    
    
    JComboBox select=null;    
    Socket clientSocket =null;
    DataInputStream data=null;
    ObjectOutputStream oos=null;
    
    String videoFile="denise.mp4";
    String HostName="localhost";
    int Port=4000;

    
    int chronoSong=0;
    int sec=0;
    int min=0;
    int heure=0;
    

    public void play(byte[] data)throws Exception{
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        Player player = new Player(in);
        player.play();
    }
    
    public Client()throws Exception{

        clientSocket = new Socket(HostName,Port);
        listener.setListener(this);
        
        data = new DataInputStream(clientSocket.getInputStream());
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        
        frame[0]=new JFrame("Streaming socket...");
        
        frame[1]=new JFrame("Picture");
        frame[2]=new JFrame("Music mp3");
        frame[3]=new JFrame("Play Video");
        
        String[] fileStr =new String[2];
        fileStr[0]= data.readUTF();
        fileStr[1]= data.readUTF();
        String video=data.readUTF();
        
        JLabel jLabel = new JLabel();
    
        JPanel panel=new JPanel();
        panel.setLayout(null);
        
        //Boutton Choice
        button[0]=new JButton("Choisir");
        button[1]=new JButton(video);
        
        button[0].setBounds(310, 80, 100, 40);

        //Boutton video
        button[1].setBounds(200, 300, 200, 50);
        
        JLabel label = new JLabel();
        JLabel labelAfa = new JLabel();
        label.setBounds(180, 10, 200, 50);
        labelAfa.setBounds(100, 300, 100, 50);

        label.setText("Image ou music?");
        labelAfa.setText("Video seulement");
        
        button[0].addActionListener(listener);
        button[1].addActionListener(listener);
        
        frame[0].setSize(500,500);

        
        select=new JComboBox(fileStr);
        select.setBounds(10, 50, 400, 20);
        
        panel.add(label);
        panel.add(labelAfa);
        
        for(int i=0 ;i<2;i++){
            panel.add(button[i]);
        }
        frame[0].add(select);
        frame[0].add(panel);
        frame[0].setVisible(true);
        frame[0].setLocationRelativeTo(null);
        frame[0].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame[0].setResizable(false);
        
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        String envoye = (String) ois.readObject();

        if(envoye.contains(".jpg") || envoye.contains(".png") ) {
           
            InputStream inputStream = clientSocket.getInputStream();
            
            byte[] sizear=new byte[4];
            inputStream.read(sizear);
            
            int size=ByteBuffer.wrap(sizear).asIntBuffer().get();
            byte[] imagear=new byte[size];
            
            inputStream.read(imagear);
            
            BufferedImage image=ImageIO.read(new ByteArrayInputStream(imagear));
            ImageIcon imageIcon = new ImageIcon(image);

            frame[1].setLayout(new FlowLayout());
            frame[1].setSize(960,636);
            
            jLabel.setIcon(imageIcon);
            
            frame[1].add(jLabel);
            frame[1].setLocationRelativeTo(null);
            frame[1].setVisible(true);
            frame[1].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame[1].setResizable(false);
        }

        if(envoye.contains(".mp3") || envoye.contains(".mkv") || envoye.contains(".wav")){ //.......
            
            int len = 1000000;
            byte[] mybytearray = new byte[len];

            frame[2].setLayout(new FlowLayout());
            frame[2].setSize(500,80);
            
            JLabel label1 = new JLabel();
            JLabel label2 = new JLabel();
            
            label1.setText(String.valueOf(sec));
            
            frame[2].add(label1);
            frame[2].setVisible(true);
            frame[2].setLocationRelativeTo(null);
            frame[2].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
            while(true && sec>=0){
                    Thread play = new Thread(new PlayMP3(mybytearray));
                    Thread.sleep(1000);
                    sec++;
                    if(sec==60){
                        sec=0;
                        min++;
                }if(min==60){
                    min=0;
                    heure++;
                }if(heure==24){
                    heure=0;
                }
                data.read(mybytearray, 0, len);
                
                play.start();
                play(mybytearray);
                System.out.println(heure+":"+min+":"+sec);
            }
        }

        if(envoye.contains(".mp4") ) {
            try {

                DataOutputStream dataOutputStream = null;
                DataInputStream dataInputStream = null;
                
                dataInputStream = new DataInputStream(clientSocket.getInputStream());
                dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("Sending...");

                int bytes = 0;
                FileOutputStream fileOutputStream = new FileOutputStream(video);
        
                long taille = dataInputStream.readLong(); 
                byte[] buffer = new byte[4 * 1024];

                while (taille > 0 && (bytes = dataInputStream.read( buffer, 0, (int)Math.min(buffer.length, taille))) != -1) {
                
                    fileOutputStream.write(buffer, 0, bytes);
                    taille -= bytes; 
                }
                System.out.println("Send finished...");
                fileOutputStream.close();
                dataInputStream.close();
                dataInputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // new Client(args);
                }
            });
        }
    }
    public void PlayVideo(){
        EmbeddedMediaPlayerComponent component = new EmbeddedMediaPlayerComponent();
        frame[3].setContentPane(component);
        frame[3].setLocation(100, 100);
        frame[3].setSize(500, 500);
        frame[3].setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame[3].setVisible(true);
        frame[3].setLocationRelativeTo(null);
        component.mediaPlayer().media().play(videoFile);
    } 
    public JButton[] getBoutton(){
        return button;
    }
    public ObjectOutputStream getObjectOutput(){
        return oos;
    }
    public JComboBox getSelected(){
        return select;
    }
}
