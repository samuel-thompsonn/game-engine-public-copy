package ooga.game.behaviors.collisioneffects;

import java.util.List;

import ooga.game.behaviors.TimeDelayedEffect;

/**
 * @author caryshindell, sam thompson
 * This represents a terrain correction effect. It is just an abstraction. It's here to show that there could be more
 * directions besides up down left right
 * Assumptions: this doesn't have to be executed by a CollisionDeterminedAction, but the results probably won't make
 * sense otherwise.
 * Dependencies: entity internal api, effect (specifically, time delayed effect)
 * Example: RunIntoTerrainLeftEffect
 */
public abstract class RunIntoTerrainEffect extends TimeDelayedEffect {

  public RunIntoTerrainEffect(List<String> args) {
    super(args);
  }
}
