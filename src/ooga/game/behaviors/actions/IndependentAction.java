package ooga.game.behaviors.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.Action;
import ooga.game.behaviors.Effect;

/**
 * @author sam thompson, caryshindell
 * IndependentAction: no other entity is necessary for the effect
 */
@SuppressWarnings("unused")
public class IndependentAction extends Action {

  public IndependentAction(List<String> args, List<Effect> effects) throws IndexOutOfBoundsException {
    super(effects);
  }

  @Override
  public List<EntityInternal> findOtherEntities(EntityInternal subject,
                                        Map<String, String> variables, Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo,
                                        GameInternal gameInternal) {
    List<EntityInternal> otherEntities = new ArrayList<>();
    otherEntities.add(null);
    return otherEntities;
  }
}
