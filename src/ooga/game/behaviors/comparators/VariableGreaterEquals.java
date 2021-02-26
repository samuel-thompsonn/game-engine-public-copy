package ooga.game.behaviors.comparators;

/**
 * @author sam thompson
 */
public class VariableGreaterEquals implements VariableComparator {

  @Override
  public boolean doubleComparison(Double o1, Double o2) {
    return (o1.compareTo(o2) > 0 || o1.equals(o2));
  }

  @Override
  public boolean stringComparison(String o1, String o2) {
    return (o1.compareTo(o2) > 0 || o1.equals(o2));
  }
}
