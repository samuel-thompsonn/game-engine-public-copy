package ooga.game.behaviors.comparators;

/**
 * @author sam thompson
 */
public class VariableEquals implements VariableComparator {

  @Override
  public boolean doubleComparison(Double o1, Double o2) {
    return compareEquals(o1,o2);
  }

  @Override
  public boolean stringComparison(String o1, String o2) {
    return compareEquals(o1,o2);
  }

  private boolean compareEquals(Object o1, Object o2) {
    return (o1.equals(o2));
  }
}
