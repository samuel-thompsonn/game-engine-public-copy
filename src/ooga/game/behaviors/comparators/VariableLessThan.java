package ooga.game.behaviors.comparators;

/**
 * @author sam thompson
 */
@SuppressWarnings("unused")
public class VariableLessThan implements VariableComparator {

  @Override
  public boolean doubleComparison(Double o1, Double o2) {
    return o1.compareTo(o2) < 0;
  }

  @Override
  public boolean stringComparison(String o1, String o2) {
    return o1.compareTo(o2) < 0;
  }
}
