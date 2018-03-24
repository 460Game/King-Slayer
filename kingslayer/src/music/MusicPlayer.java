package music;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlayer {

  private static Thread music = new Thread();

  public static synchronized void playIntroMusic() {
    try {
      music.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    music = new Thread(){
      public void run() {
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(
              MusicPlayer.class.getResourceAsStream("Quest.wav"));
          clip.open(inputStream);
          clip.start();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();
  }

  public static synchronized void playGameMusic() {
    try {
      music.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    music = new Thread(){
      public void run() {
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(
              MusicPlayer.class.getResourceAsStream("PirateCrew.wav"));
          clip.open(inputStream);
          clip.start();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    };
    music.start();
  }

}
