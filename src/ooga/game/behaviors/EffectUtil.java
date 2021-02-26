package ooga.game.behaviors;

import java.util.List;

/**
 * @author sam thompson
 * Contains static utilities that are shared among multiple Effects.
 */
public class EffectUtil {

  private EffectUtil() {
    //A private constructor exists to prevent an implicit public constructor,
    // since this is a utility class.
  }

  /**
   * @param vector The vector to calculate the magnitude for.
   * @return The magnitude of the vector (distance of the point from the origin).
   */
  public static double getMagnitude(List<Double> vector) {
    return Math.sqrt(Math.pow(vector.get(0),2) + Math.pow(vector.get(1),2));
  }

  /**
   * Returns the dot product of two 2D vectors. Ignores elements beyond the first two in the lists.
   * @param a The first list to carry out the dot product for.
   * @param b The second list to carry out the dot product for.
   * @return The dot product of the vectors consisting of the first two elements of a and b.
   */
  public static double getDotProduct(List<Double> a, List<Double> b) {
    return (a.get(0) * b.get(0)) + (a.get(1) * b.get(1));
  }
}
