package mp3Play;

import java.io.*;
import javax.sound.sampled.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PlayMP3 implements Runnable {

    byte[] taille;

    public PlayMP3(byte[] taille) {
        this.taille = taille;
    }

    public void run() {
        try {
            play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() throws Exception {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(this.taille));
        Player player = new Player(data);
        player.play();
    }
    
}
