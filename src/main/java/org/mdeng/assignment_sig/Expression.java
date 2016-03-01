package org.mdeng.assignment_sig;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Supposing ops are all lower case.
 * Supposing variable names are case sensitive
 *
 *
 */
public class Expression {

    private static final Pattern PATTERN_EXPRESSION_OP = Pattern.compile("^\\s*(add|sub|mult|div)\\s*\\(\\s*(.+)\\s*\\)\\s*$");
    private static final Pattern PATTERN_EXPRESSION_LET = Pattern.compile("^\\s*let\\s*\\(\\s*(\\w+)\\s*,\\s*(.+)\\s*\\)\\s*$");
    private static final Pattern PATTERN_NUMBER = Pattern.compile("\\s*(-?\\d+)\\s*");
    private static final Pattern PATTERN_VARIABLE = Pattern.compile("\\s*([a-zA-Z]+)\\s*");

    private static final String OP_ADD = "add";
    private static final String OP_SUB = "sub";
    private static final String OP_MULT = "mult";
    private static final String OP_DIV = "div";

    private String mString;
    private HashMap<String, Integer> mVariables = new HashMap<>();

    public Expression(final String str) {
        mString = str;
    }

    public Expression(final String str, final HashMap<String, Integer> variables) {
        mString = str;
        mVariables = new HashMap<>();
        // We need our own copy of mVariable for this Expression object.
        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            mVariables.put(entry.getKey(), entry.getValue());
        }
    }

    public int parse() {
        System.out.println("Going to parse: #" + mString + "#");

        Matcher m = PATTERN_EXPRESSION_OP.matcher(mString);
        if (m.find()) {
            String op = m.group(1);
            System.out.println("Got op=" + op);

            String argStr = m.group(2);
            System.out.println("Got arg string=" + argStr);

            String[] args = splitByComma(argStr);

            System.out.println("Got arg1=" + args[0]);
            System.out.println("Got arg2=" + args[1]);

            Expression expression1 = new Expression(args[0], mVariables);
            Expression expression2 = new Expression(args[1], mVariables);

            if (op.equals(OP_ADD)) {
                return expression1.parse() + expression2.parse();
            } else if (op.equals(OP_SUB)) {
                return expression1.parse() - expression2.parse();
            } else if (op.equals(OP_MULT)) {
                return expression1.parse() * expression2.parse();
            } else if (op.equals(OP_DIV)) {
                return expression1.parse() / expression2.parse();
            }

            // Shouldn't reach here.
            throw new RuntimeException("Internal Error");
        }

        m = PATTERN_EXPRESSION_LET.matcher(mString);
        if (m.find()) {
            String variableName = m.group(1);
            System.out.println("Got variable name=" + variableName);

            String argStr = m.group(2);
            System.out.println("Got arg string=" + argStr);

            String[] args = splitByComma(argStr);

            System.out.println("Got arg1=" + args[0]);
            Expression expressionValue = new Expression(args[0], mVariables);
            int value = expressionValue.parse();
            // Inner variable will overwrite the variable with the same name in outer scope.
            mVariables.put(variableName, value);

            System.out.println("Got arg2=" + args[1]);
            Expression expression = new Expression(args[1], mVariables);

            return expression.parse();
        }

        m = PATTERN_VARIABLE.matcher(mString);
        if (m.find()) {
            String variableName = m.group(1);
            System.out.println("Got variable name=" + variableName);

            if (mVariables.containsKey(variableName)) {
                return mVariables.get(variableName);
            } else {
                throw new RuntimeException("Invalid variable name: " + variableName);
            }
        }

        m = PATTERN_NUMBER.matcher(mString);
        if (m.find()) {
            try {
                int n = Integer.parseInt(m.group(1));
                return n;
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid number format.", e);
            }
        }

        throw new RuntimeException("Invalid syntax.");
    }

    private static String[] splitByComma(final String str) {
        String[] expressions = new String[2];
        // Find first comma which is not in any bracket
        int layersInBracket = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == ',' && layersInBracket == 0) {
                expressions[0] = str.substring(0, Math.max(i, 0));
                expressions[1] = str.substring(Math.min(i+1, str.length() -1));
                return expressions;
            }
            if (ch == '(') {
                layersInBracket++;
            } else if (ch == ')') {
                layersInBracket--;
            }
        }

        throw new RuntimeException("Invalid syntax: \"" + str + "\" should be two expression separated by comma");
    }
}
