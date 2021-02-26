package ooga.game.behaviors;

import java.util.List;
import java.util.ResourceBundle;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;

import java.util.Map;

/**
 * @author caryshindell, sam thompson
 * This is an abstract class that allows an effect to take in a time delay as its final optional argument.
 * The effect will keep track of the time passed and only execute when the specified delay has passed.
 * One small design flaw with this is you can't control how the time passed gets reset. So if the effect is intended
 * to only happen when you've been colliding with x for 5 seconds, you can just collide with it for 2.5, stop, then 2.5 again
 * Note: this also uses reflection to determine the default number of arguments for each effect, which are stored in a
 * properties file
 * Dependencies: effect
 */
public abstract class TimeDelayedEffect implements Effect {

  public static final String PATH_TO_DEFAULTS = "ooga/game/behaviors/resources/effectdefaults";
  private double timePassed = 0.0;
  private String timeDelayData = "";

  /**
   * @param args The String arguments of this effect.
   */
  public TimeDelayedEffect(List<String> args) {
    processArgs(args);
    ResourceBundle resources = ResourceBundle.getBundle(PATH_TO_DEFAULTS);
    processTimeDelay(args, Integer.parseInt(resources.getString(this.getClass().getSimpleName())));
  }

  /**
   * Processes the String arguments given in the data file into values used by this effect.
   * @param args The String arguments given for this effect in the data file.
   */
  public abstract void processArgs(List<String> args);

  /**
   * Setter
   * @param timeDelayData this should be the time passed THAT THIS EFFECT HAS BEEN RUNNING. So if the effect was not being executed,
   *                  that time will not count toward the time passed.
   */
  public void setTimeDelay(String timeDelayData){
    this.timeDelayData = timeDelayData;
  }

  private boolean hasTimeDelayFinished(double elapsedTime, EntityInternal subject, Map<String, String> variables) {
    timePassed += elapsedTime;
    double timeDelay = parseData(timeDelayData, subject, variables, 0.0);
    return timePassed >= timeDelay;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void doEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game){
    if(hasTimeDelayFinished(elapsedTime, subject, variables)){
      doTimeDelayedEffect(subject, otherEntity, elapsedTime, variables, game);
      timePassed = 0.0;
    }
  }

  /**
   * Does the same thing as doEffect, but with the delay that was specified when the effect was created.
   * @see Effect#doEffect
   */
  @SuppressWarnings("unused")
  protected abstract void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game);

  protected void processTimeDelay(List<String> args, int numDefaultArgs) {
    if (args.size() > numDefaultArgs) {
      setTimeDelay(args.get(numDefaultArgs));
    }
  }
}
