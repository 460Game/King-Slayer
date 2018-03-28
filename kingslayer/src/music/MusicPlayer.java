package music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.BufferedInputStream;
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
      gameClip = AudioSystem.getClip();
      InputStream audioSrc = MusicPlayer.class.getResourceAsStream("PirateCrew.wav");
      InputStream bufferedIn = new BufferedInputStream(audioSrc);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
      gameClip.open(audioStream);

      intro = AudioSystem.getClip();
      audioSrc = MusicPlayer.class.getResourceAsStream("Quest.wav");
      bufferedIn = new BufferedInputStream(audioSrc);
      audioStream = AudioSystem.getAudioInputStream(bufferedIn);
      intro.open(audioStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static synchronized void playIntroMusic() {
    if (gameClip.isRunning())
      gameClip.stop();
    music = new Thread(() -> {
      try {
        intro.setFramePosition(0);
        intro.loop(Clip.LOOP_CONTINUOUSLY);
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    });
    music.start();
  }

  public static synchronized void playGameMusic() {
    if (intro.isRunning())
      intro.stop();
    music = new Thread(() -> {
      try {
        gameClip.setFramePosition(0);
        gameClip.loop(Clip.LOOP_CONTINUOUSLY);
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    });
    music.start();
  }

  public static synchronized void playArrowSound() {
    music = new Thread(() -> {
      try {
        bow = AudioSystem.getClip();
        InputStream audioSrc = MusicPlayer.class.getResourceAsStream("Bow.wav");
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
        bow.open(audioStream);
        bow.start();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    });
    music.start();
  }

  public static synchronized void playChargeSound() {
    music = new Thread(() -> {
      try {
        charge = AudioSystem.getClip();
        InputStream audioSrc = MusicPlayer.class.getResourceAsStream("Swish.wav");
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
        charge.open(audioStream);
        charge.start();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    });
    music.start();
  }

  public static synchronized void playSellSound() {
    music = new Thread(() -> {
      try {
        sell = AudioSystem.getClip();
        InputStream audioSrc = MusicPlayer.class.getResourceAsStream("Sell.wav");
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
        sell.open(audioStream);
        sell.start();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    });
    music.start();
  }

  public static synchronized void playConstructionSound() {
    music = new Thread(() -> {
      try {
        construction = AudioSystem.getClip();
        InputStream audioSrc = MusicPlayer.class.getResourceAsStream("Construction.wav");
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
        construction.open(audioStream);
        construction.start();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    });
    music.start();
  }

  public static synchronized void playErrorSound() {
    music = new Thread(() -> {
      try {
        error = AudioSystem.getClip();
        InputStream audioSrc = MusicPlayer.class.getResourceAsStream("Error.wav");
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
        error.open(audioStream);
        error.start();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    });
    music.start();
  }

}
