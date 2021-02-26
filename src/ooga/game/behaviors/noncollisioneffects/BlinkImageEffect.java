package ooga.game.behaviors.noncollisioneffects;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.Effect;

import java.util.List;
import java.util.Map;

/**
 * @author caryshindell
 * Blinks the image between 2 different images at a set rate, until total blink time has passed.
 * Note: this does not implement TimeDelayedEffect because it has its own time delays
 * This can be coupled with another time delayed effect like respawn (but this effect must complete before respawn)
 * Dependencies: effect, entity internal
 * Example: when fireboy touches water his image flickers for a few seconds to show that he died
 */
@SuppressWarnings("unused")
public class BlinkImageEffect implements Effect {

  final String image1Data;
  final String image2Data;
  final String finalImageData;
  final String blinkTimeDelayData;
  final String totalBlinkTimeData;
  double timePassed = 0.0;
  double timePassedSinceLastBlink = 0.0;
  String currentImage;

  public BlinkImageEffect(List<String> args) throws IndexOutOfBoundsException{
    int index = 0;
    image1Data = args.get(index++);
    image2Data = args.get(index++);
    finalImageData = args.get(index++);
    blinkTimeDelayData = args.get(index++);
    totalBlinkTimeData = args.get(index);
    currentImage = image1Data;
  }

  /**
   * Requires the effect to have a reference to the
   * instance that uses it in order to have an effect on that instance.
   * @param subject     The entity that owns this controls behavior. This is the entity that should
   *                    be modified.
   * @param otherEntity
   * @param elapsedTime
   * @param variables
   * @param game
   */
  @Override
  public void doEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {
    double blinkTimeDelay = parseData(blinkTimeDelayData, subject, variables, 0.0);
    double totalBlinkTime = parseData(totalBlinkTimeData, subject, variables, 0.0);
    String finalImage = Effect.doVariableSubstitutions(finalImageData, subject, variables);
    String image1 = Effect.doVariableSubstitutions(image1Data, subject, variables);
    String image2 = Effect.doVariableSubstitutions(image2Data, subject, variables);
    timePassedSinceLastBlink += elapsedTime;
    if(timePassed >= totalBlinkTime){
      SetImageEffect.setImage(subject, finalImage, variables, this);
      timePassed = 0.0;
      /* note that due to this method only being called when the behavior conditions are true means timePassed will be
       somewhat meaningless; that is, this effect is unlikely to give you the desired final image unless the numbers
       are perfect
       */
    }
    else if(timePassedSinceLastBlink >= blinkTimeDelay){
      if(currentImage.equals(image1)) {
        SetImageEffect.setImage(subject, image2, variables, this);
        currentImage = image2;
      } else{
        SetImageEffect.setImage(subject, image1, variables, this);
        currentImage = image1;
      }
      timePassedSinceLastBlink = 0.0;
    }
    timePassed += elapsedTime;
  }
}
