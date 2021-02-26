package ooga.game.behaviors.comparators;

/**
 * @author sam thompson
 * Compares two variables and returns true if the conditions defined in the comparator's
 * implementation are true. Since variables can be Strings or Doubles (as Strings), it must
 * be able to compare either, even if it's primarily used for one.
 */
public interface VariableComparator  {

  /**
   * This interface is similar to Comparator, but it returns a boolean rather than an int, which
   * means that we can avoid using if statements that check ints and return ints.
   * @param first The first value to compare
   * @param second The second value to compare
   * @return True if the requirements of the comparator implementation are met.
   */
  default boolean compareVars(String first, String second) {
    if (first == null || second == null) {
      return false;
    }
    try {
      Double d1 = Double.parseDouble(first);
      Double d2 = Double.parseDouble(second);
      return doubleComparison(d1,d2);
    } catch (NumberFormatException e) {
      return stringComparison(first,second);
    }
  }

  /**
   * Handles the comparison if it is possible to parse both variables into doubles.
   * @param o1 The first Double in the comparison.
   * @param o2 The second Double in the comparison.
   * @return True if the comparison requirement has been met and the condition is satisfied.
   */
  boolean doubleComparison(Double o1, Double o2);

  /**
   * Handles the comparison if it is not possible to parse both variables into doubles.
   * @param o1 The first String in the comparison.
   * @param o2 The second String in the comparison.
   * @return True if the comparison requirement has been met and the condition is satisfied.
   */
  boolean stringComparison(String o1, String o2);
}
