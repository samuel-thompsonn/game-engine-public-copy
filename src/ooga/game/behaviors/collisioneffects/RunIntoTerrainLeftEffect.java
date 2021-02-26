package ooga.game.behaviors.collisioneffects;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;

import java.util.List;
import java.util.Map;

/**
 * @author caryshindell, sam thompson
 * This represents an effect where, typically, subject entity is moving up into the terrain entity
 * The subject will get moved back directly below the terrain.
 * Assumptions: this doesn't have to be executed by a CollisionDeterminedAction, but the results probably won't make
 * sense otherwise.
 * Dependencies: entity internal api, effect
 * Example: mario is jumping up into a brick. This effect makes sure he doesn't pass through it.
 */
@SuppressWarnings("unused")
public class RunIntoTerrainLeftEffect extends RunIntoTerrainEffect {
  public RunIntoTerrainLeftEffect(List<String> args) {
    super(args);
  }

  @Override
  public void processArgs(List<String> args) {
    //has no arguments
  }

  /**
   * Performs the effect
   * @param subject     The entity that owns this. This is the entity that should be modified.
   * @param otherEntity entity we are "interacting with" in this effect
   * @param elapsedTime time between steps in ms
   * @param variables   game variables
   * @param game        game instance
   */
  @Override
  protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {
    double targetX = otherEntity.getPosition().get(0) + otherEntity.getWidth();
    double targetY = subject.getPosition().get(1);
    subject.setPosition(List.of(targetX,targetY));
    subject.blockInDirection("Left", true);
  }
}
