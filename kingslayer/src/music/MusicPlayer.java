package music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class MusicPlayer {

  private static Thread music = new Thread();
  private static Clip intro;
  private static Clip gameClip;

  private static Clip error;
  private static Clip bow;
  private static Clip charge;
  private static Clip sell;
  private static Clip construction;

  static {
    try {
      /*gameClip = AudioSystem.getClip();
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(MusicPlayer.class.getResourceAsStream("PirateCrew.wav"));
      gameClip.open(inputStream);

      intro = AudioSystem.getClip();
      inputStream = AudioSystem.getAudioInputStream(MusicPlayer.class.getResourceAsStream("Quest.wav"));
      intro.open(inputStream);

      error = AudioSystem.getClip();
      inputStream = AudioSystem.getAudioInputStream(MusicPlayer.class.getResourceAsStream("Error.wav"));
      error.open(inputStream);

      bow = AudioSystem.getClip();
      inputStream = AudioSystem.getAudioInputStream(MusicPlayer.class.getResourceAsStream("Bow.wav"));
      bow.open(inputStream);

      charge = AudioSystem.getClip();
      inputStream = AudioSystem.getAudioInputStream(MusicPlayer.class.getResourceAsStream("Swish.wav"));
      charge.open(inputStream);

      sell = AudioSystem.getClip();
      inputStream = AudioSystem.getAudioInputStream(MusicPlayer.class.getResourceAsStream("Sell.wav"));
      sell.open(inputStream);

      construction = AudioSystem.getClip();
      inputStream = AudioSystem.getAudioInputStream(MusicPlayer.class.getResourceAsStream("Construction.wav"));
      construction.open(inputStream);*/
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static synchronized void playIntroMusic() {
    /*if (gameClip.isRunning())
      gameClip.stop();
    intro.setFramePosition(0);
    music = new Thread(){
      public void run() {
        try {
          intro.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();*/
  }

  public static synchronized void playGameMusic() {
    /*if (intro.isRunning())
      intro.stop();
    gameClip.setFramePosition(0);
    music = new Thread(){
      public void run() {
        try {
          gameClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();*/
  }

  public static synchronized void playArrowSound() {
    /*bow.setFramePosition(0);
    music = new Thread(){
      public void run() {
        try {
          bow.start();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();*/
  }

  public static synchronized void playChargeSound() {
    /*charge.setFramePosition(0);
    music = new Thread(){
      public void run() {
        try {
          charge.start();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();*/
  }

  public static synchronized void playSellSound() {
    /*sell.setFramePosition(0);
    music = new Thread(){
      public void run() {
        try {
          sell.start();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();*/
  }

  public static synchronized void playConstructionSound() {
    /*construction.setFramePosition(0);
    music = new Thread(){
      public void run() {
        try {
          construction.start();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();*/
  }

  public static synchronized void playErrorSound() {
    /*error.setFramePosition(0);
    music = new Thread(){
      public void run() {
        try {
          error.start();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();*/
  }

}
