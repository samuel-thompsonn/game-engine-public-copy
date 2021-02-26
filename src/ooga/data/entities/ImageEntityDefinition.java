package ooga.data.entities;

import ooga.game.behaviors.ConditionalBehavior;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caryshindell, sam thompson, braeden ward
 * This is the outline of an entity that can be defined in the data files, to make it faster to instantiate specific
 * instances. Note that it contains behaviors and does not contain position.
 * Assumptions: we are essentially assuming that all image entities with the same name/type have the same behaviors
 * Dependencies: ConditionalBehavior interface
 * Example: mario has a set image, width, height, behaviors, and camera stationary status.
 */
public class ImageEntityDefinition {
    private final double myHeight;
    private final double myWidth;
    private final String myImageLocation;
    private final List<ConditionalBehavior> myBehaviors;
    private final String myName;
    private Map<String, String> myVariables = new HashMap<>();
    private boolean stationary = false;

    /**
     * @param name The Name identifying the type of entity.
     * @param height The starting height of entity instances.
     * @param width The starting height of entity instances.
     * @param imageLocation The String with the filepath of this entity's image.
     * @param behaviors The List of conditional behaviors that entity instances will execute.
     */
    public ImageEntityDefinition(String name, double height, double width, String imageLocation, List<ConditionalBehavior> behaviors){
        myName = name;
        myHeight = height;
        myWidth = width;
        myImageLocation = imageLocation;
        myBehaviors = behaviors;
    }

    /**
     * Creates an instance of this Entity type at a specified location.
     * @param xpos The X position of the created instance.
     * @param ypos The Y position of the created instance.
     * @return An instance of this definition's type at the specified location.
     */
    public ImageEntity makeInstanceAt(Double xpos, Double ypos){
        ImageEntity newEntity = new ImageEntity(myName, myImageLocation, xpos, ypos, myWidth, myHeight);
        newEntity.setConditionalBehaviors(myBehaviors);
        newEntity.setVariables(myVariables);
        newEntity.makeNonStationaryProperty(stationary);
        return newEntity;
    }

    /**
     * @return The String representation of this entity type.
     */
    @Override
    public String toString() {
        return myName + ": (" + myHeight + " x " + myWidth + ")";
    }

    /**
     * @return The starting height of entity instances.
     */
    public double getHeight() {
        return myHeight;
    }

    /**
     * @return The starting height of entity instances.
     */
    public double getWidth() {
        return myWidth;
    }

    /**
     * @param entityVariables The starting names and values of variables created instances should have.
     */
    public void setVariables(Map<String, String> entityVariables) { myVariables = entityVariables; }

    /**
     * @param isStationary True if this entity should NOT move onscreen based on camera moves.
     */
    public void setStationary(boolean isStationary){stationary = isStationary;}

    /**
     * @return True if this entity does NOT move onscreen based on camera moves.
     */
    public boolean getStationary() {
        return stationary;
    }
}
