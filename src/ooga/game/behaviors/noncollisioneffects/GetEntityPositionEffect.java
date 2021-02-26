package ooga.game.behaviors.noncollisioneffects;

import ooga.Entity;
import ooga.game.EntityInternal;
import ooga.game.GameInternal;
import ooga.game.behaviors.TimeDelayedEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unused")
public class GetEntityPositionEffect extends TimeDelayedEffect {

    private List<String> desiredLocation;

    /**
     * Construct the set position effect by setting desiredLocation. Note that it adds strings because it could depend on variables.
     * @param args list of arguments from XMLGameDataReader
     */
    public GetEntityPositionEffect(List<String> args) throws IndexOutOfBoundsException{
        super(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processArgs(List<String> args) {
        desiredLocation = new ArrayList<>();
        desiredLocation.add(args.get(0));
        desiredLocation.add(args.get(1));
    }

    @Override
    protected void doTimeDelayedEffect(EntityInternal subject, EntityInternal otherEntity, double elapsedTime, Map<String, String> variables, GameInternal game) {

    }

    /**
     * Sets the location to the stored location. If stored location is variable dependent, attempts to find a matching game variable.
     * If there is none, attempts to find a matching entity variable. If there is none, defaults to 0.0.
     * @param subject     The entity that owns this. This is the entity that should be modified.
     * @param otherEntity entity we are "interacting with" in this effect
     * @param elapsedTime time between steps in ms
     * @param variables   game variables
     * @param game        game instance
     */
}
