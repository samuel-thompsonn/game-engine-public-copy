package ooga.game.behaviors.actions;

import java.util.List;
import java.util.Map;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.Action;
import ooga.game.behaviors.Effect;

/**
 * @author sam thompson, caryshindell
 * NameDeterminedAction: executes the effect on all entities with the specified name "howToFind"
 */
@SuppressWarnings("unused")
public class NameDeterminedAction extends Action {

  final String myTargetName;

  public NameDeterminedAction(List<String> args, List<Effect> effects) throws IndexOutOfBoundsException {
    super(effects);
    myTargetName = args.get(0);
  }

  @Override
  public List<EntityInternal> findOtherEntities(EntityInternal subject,
                                        Map<String, String> variables, Map<EntityInternal, Map<String, List<EntityInternal>>> collisionInfo,
                                        GameInternal gameInternal) {
    return gameInternal.getEntitiesWithName(myTargetName);
  }
}
