package game.model.game.model.worldObject.entity.drawStrat;

public class AnimationDrawData extends DrawData {
  // Only used for animated entities
  boolean animated;
  int imageNum;
  public char direction;
  int count;

  public AnimationDrawData() {

  }

  private AnimationDrawData(boolean animated) {
    this.animated = animated;
    imageNum = 0;
    direction = 'S';
    count = 0;
  }

  public static AnimationDrawData makeAnimated() {
    return new AnimationDrawData(true);
  }
}
