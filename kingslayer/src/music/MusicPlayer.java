package music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MusicPlayer {

  private static Thread music = new Thread();
  private static MediaPlayer intro = new MediaPlayer(new Media(new File("kingslayer/resources/music/Quest.mp3").toURI().toString()));

  public static synchronized void playIntroMusic() {
    music = new Thread(){
      public void run() {
        try {
          intro.play();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();
  }

  public static synchronized void playGameMusic() {
    intro.stop();
    music = new Thread(){
      public void run() {
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(
              MusicPlayer.class.getResourceAsStream("PirateCrew.wav"));
          clip.open(inputStream);
          clip.loop(1000);
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();
  }

}
