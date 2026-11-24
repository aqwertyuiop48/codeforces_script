package agent;

import com.fasterxml.jackson.databind.JsonNode;

/** Safe arithmetic evaluator (recursive descent): + - * / ^ ( ) and unary minus. */
public class CalculatorTool implements Tool {
    public String name() { return "calculator"; }
    public String description() { return "Evaluate arithmetic. Input: {\"expression\":\"2*(3+4)\"}"; }

    public String run(JsonNode input) {
        double v = eval(input.path("expression").asText());
        return v == Math.rint(v) && Math.abs(v) < 1e15 ? String.valueOf((long) v) : String.valueOf(v);
    }

    public static double eval(String s) {
        return new Object() {
            int pos = -1, ch;
            void next() { ch = ++pos < s.length() ? s.charAt(pos) : -1; }
            boolean eat(int c) {
                while (ch == ' ') next();
                if (ch == c) { next(); return true; }
                return false;
            }
            double parse() {
                next();
                double x = expr();
                if (pos < s.length()) throw new IllegalArgumentException("Unexpected: " + (char) ch);
                return x;
            }
            double expr() {
                double x = term();
                for (;;) {
                    if (eat('+')) x += term();
                    else if (eat('-')) x -= term();
                    else return x;
                }
            }
            double term() {
                double x = factor();
                for (;;) {
                    if (eat('*')) x *= factor();
                    else if (eat('/')) {
                        double d = factor();
                        if (d == 0) throw new ArithmeticException("Division by zero");
                        x /= d;
                    } else return x;
                }
            }
            double factor() {
                if (eat('+')) return factor();
                if (eat('-')) return -factor();
                double x;
                int start = pos;
                if (eat('(')) {
                    x = expr();
                    if (!eat(')')) throw new IllegalArgumentException("Missing )");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') next();
                    x = Double.parseDouble(s.substring(start, pos));
                } else {
                    throw new IllegalArgumentException("Unexpected: " + (ch == -1 ? "end" : String.valueOf((char) ch)));
                }
                if (eat('^')) x = Math.pow(x, factor());
                return x;
            }
        }.parse();
    }
}
