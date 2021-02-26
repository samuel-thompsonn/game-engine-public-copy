package ooga.game.behaviors.collisioneffects;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;

import java.util.List;
import java.util.Map;

/**
 * @author caryshindell, sam thompson
 * This represents an effect where, typically, subject entity is moving down into the terrain entity
 * The subject will get moved back directly above the terrain.
 * Assumptions: this doesn't have to be executed by a CollisionDeterminedAction, but the results probably won't make
 * sense otherwise.
 * Dependencies: entity internal api, effect
 * Example: mario is standing on the ground. This effect makes sure he doesn't fall through it due to gravity.
 */
@SuppressWarnings("unused")
public class RunIntoTerrainDownEffect extends RunIntoTerrainEffect {
  public RunIntoTerrainDownEffect(List<String> args) {
    super(args);
  }

  @Override
  public void processArgs(List<String> args) {
    //has no arguments
  }

  /**
   * Performs the effect
   * @param subject     The entity that owns this. This is the entity that should be modified.
   * @param otherEntity
   * @param elapsedTime
   * @param variables
   * @param game
   */
  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {
    //subject.setVelocity(subject.getVelocity().get(0),0);
    double targetX = subject.getPosition().get(0);
    double targetY = otherEntity.getPosition().get(1)-subject.getHeight();
    subject.setPosition(List.of(targetX,targetY));
    subject.blockInDirection("Down", true);
  }
}
