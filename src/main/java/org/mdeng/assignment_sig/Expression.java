package org.mdeng.assignment_sig;

/**
 * The MIT License (MIT)
 * Copyright (c) 2016 mdeng

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

import static org.mdeng.assignment_sig.LogUtil.log;
import static org.mdeng.assignment_sig.LogUtil.VerbosityLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An object to parse an expression string.
 *
 * Some clarifications:
 * 1. Supposing ops("add", "sub", etc) are all lower case and can't be used for variable names.
 * 2. Supposing variable names are case sensitive.
 * 3. Inner variable will overwrite the variables with the same name in outer scope.
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

    /**
     * It contains the string to be parsed and evaluated.
     */
    private String mString;
    /**
     * Variables defined in the current scope, with variable name as hash key and variable value as hash value.
     */
    private HashMap<String, Integer> mVariables = new HashMap<>();

    /**
     * @param str - the string to be evaluated.
     */
    public Expression(final String str) {
        mString = str;
    }

    /**
     * @param str - the string to be evaluated.
     * @param variables - Variables defined in enclosing scope, with variable name as hash key and variable value as hash value.
     */
    public Expression(final String str, final HashMap<String, Integer> variables) {
        mString = str;
        mVariables = new HashMap<>();
        // We need our own copy of mVariable for this Expression object.
        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            mVariables.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * This method will parse the expression, evaluate variables and calculate the final resulting integer value.
     * Note: this method works recursively.
     *
     * @return the calculated result of the expression
     */
    int parse() {
        log(VerbosityLevel.DEBUG, "parse(): ###" + mString + "###");
        int retValue = Integer.MIN_VALUE;

        Matcher m = PATTERN_EXPRESSION_OP.matcher(mString);
        if (m.find()) {
            String op = m.group(1);
            String argStr = m.group(2);

            String[] args = splitByComma(argStr);
            Expression expression1 = new Expression(args[0], mVariables);
            Expression expression2 = new Expression(args[1], mVariables);

            if (op.equals(OP_ADD)) {
                retValue = expression1.parse() + expression2.parse();
            } else if (op.equals(OP_SUB)) {
                retValue = expression1.parse() - expression2.parse();
            } else if (op.equals(OP_MULT)) {
                retValue = expression1.parse() * expression2.parse();
            } else if (op.equals(OP_DIV)) {
                retValue = expression1.parse() / expression2.parse();
            } else {
                // Shouldn't reach here.
                String errMsg = "Internal Error";
                log(VerbosityLevel.ERROR, "parse(): ###" + mString + "### returns " + errMsg);
                throw new RuntimeException(errMsg);
            }

            log(VerbosityLevel.DEBUG, "parse(): ###" + mString + "### returns " + retValue);
            return retValue;
        }

        m = PATTERN_EXPRESSION_LET.matcher(mString);
        if (m.find()) {
            String variableName = m.group(1);
            String argStr = m.group(2);

            String[] args = splitByComma(argStr);
            Expression expressionValue = new Expression(args[0], mVariables);
            int value = expressionValue.parse();
            // Inner variable will overwrite the variable with the same name in outer scope.
            mVariables.put(variableName, value);

            Expression expression = new Expression(args[1], mVariables);

            retValue = expression.parse();
            log(VerbosityLevel.DEBUG, "parse(): ###" + mString + "### returns " + retValue);
            return retValue;
        }

        m = PATTERN_VARIABLE.matcher(mString);
        if (m.find()) {
            String variableName = m.group(1);

            if (mVariables.containsKey(variableName)) {
                retValue =  mVariables.get(variableName);
                log(VerbosityLevel.DEBUG, "parse(): ###" + mString + "### returns " + retValue);
                return retValue;
            } else {
                String errMsg = "Invalid input - invalid variable name: " + variableName;
                log(VerbosityLevel.INFO, errMsg);
                throw new RuntimeException(errMsg);
            }
        }

        m = PATTERN_NUMBER.matcher(mString);
        if (m.find()) {
            try {
                retValue = Integer.parseInt(m.group(1));
                log(VerbosityLevel.DEBUG, "parse(): ###" + mString + "### returns " + retValue);
                return retValue;
            } catch (NumberFormatException e) {
                String errMsg = "Invalid input - invalid number format! " + e.toString();
                log(VerbosityLevel.INFO, errMsg);
                throw new RuntimeException(errMsg);
            }
        }

        String errMsg = "Invalid input!";
        log(VerbosityLevel.INFO, errMsg);
        throw new RuntimeException(errMsg);
    }

    /**
     * Java regexp will not find the right comma as we need when there are multiple commas in the string. Our problem is
     * more specific, i.e., we need to find the comma of the top level, i.e., outside any brackets.
     *
     * @param str - the string we want to find the top level comma(there should be only one in our case)
     * @return an array of string of two elements, with the first element being the part of string in front of _the_ comma,
     *         and the second element being the part of string after _the_ comma.
     */
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

        throw new RuntimeException("Invalid input - \"" + str + "\" should be two expression separated by comma!");
    }

}
