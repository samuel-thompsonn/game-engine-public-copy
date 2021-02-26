package ooga.game.behaviors;

/**
 * I (CARY) DID NOT WRITE THIS CODE
 * it came from: https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form
 * It is just an expression parser so that we can turn "5+5" into 10
 */
public interface ExpressionEvaluator {

  static double eval(final String str) {
    return new Object() {
      int pos = -1, ch;
      void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
      }
      boolean eat(int charToEat) {
        while (ch == ' ') nextChar();
        if (ch == charToEat) {
          nextChar();
          return true;
        }
        return false;
      }
      double parse() {
        nextChar();
        double x = parseExpression();
        if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
        return x;
      }
      double parseExpression() {
        double x = parseTerm();
        for (;;) {
          if      (eat('+')) x += parseTerm(); // addition
          else if (eat('-')) x -= parseTerm(); // subtraction
          else return x;
        }
      }
      double parseTerm() {
        double x = parseFactor();
        for (;;) {
          if      (eat('*')) x *= parseFactor(); // multiplication
          else if (eat('/')) x /= parseFactor(); // division
          else return x;
        }
      }
      double parseFactor() {
        if (eat('+')) return parseFactor(); // unary plus
        if (eat('-')) return -parseFactor(); // unary minus
        double x;
        int startPos = this.pos;
        if (eat('(')) { // parentheses
          x = parseExpression();
          eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
          while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
          x = Double.parseDouble(str.substring(startPos, this.pos));
        } else if (ch >= 'a' && ch <= 'z') { // functions
          while (ch >= 'a' && ch <= 'z') nextChar();
          x = parseFactor();
          // removed the parts with sqrt, sin, tan, cos
        } else {
          throw new RuntimeException("Unexpected: " + (char)ch);
        }
        if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
        return x;
      }
    }.parse();
  }
}
