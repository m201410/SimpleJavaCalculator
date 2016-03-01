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

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mdeng.assignment_sig.LogUtil.log;
import static org.mdeng.assignment_sig.LogUtil.VerbosityLevel;

public class Main {
    /** for unit test */
    static VerbosityLevel mCurrentLevel = VerbosityLevel.ERROR;
    /** for unit test */
    static String mExpressionString = new String();

    public static void main(String[] args) {
        LogUtil.setVerbosityLevel(mCurrentLevel);

        if (args.length < 1) {
            String errorMsg = "Invalid input - need at lease one string as expression";
            log(VerbosityLevel.INFO, errorMsg);
            throw new RuntimeException(errorMsg);
        }

        for(int i = 0; i < args.length; i++) {
            log(VerbosityLevel.DEBUG, "input argument[" + i + "]: ###" + args[i] + "###");
        }

        boolean havingVerbosity = false;
        VerbosityLevel assignedLevel = getVerbosityLevel(args);
        if (assignedLevel != VerbosityLevel.INVALID && assignedLevel != LogUtil.getVerbosityLevel()) {
            mCurrentLevel = assignedLevel;
            LogUtil.setVerbosityLevel(assignedLevel);
            log(VerbosityLevel.DEBUG, "Verbosity is set to level " + assignedLevel.getValue());
            havingVerbosity = true;
        }

        mExpressionString = args[havingVerbosity ? 1 : 0];
        log(VerbosityLevel.INFO, "input expression: ###" + mExpressionString + "###");
        Expression expression = new Expression(mExpressionString);
        int value = expression.parse();
        System.out.println(value);
    }

    /**
     * Retrive verbosity level from argument list. Otherwise return current default level.
     * @param args -- A valid input argument string with verbosity level should be looking like: '-v2 "add(1, 2)"'
     * @return the level if present, otherwise current default level.
     */
    private static VerbosityLevel getVerbosityLevel(String[] args) {
        VerbosityLevel level = LogUtil.getVerbosityLevel();

        final Pattern pattern = Pattern.compile("-v(\\d)");
        Matcher m = pattern.matcher(args[0]);
        if (m.find()) {
            final String levelStr = m.group(1);
            int newLevel = Integer.parseInt(levelStr);
            if (LogUtil.VerbosityLevel.isValid(newLevel)) {
                level = VerbosityLevel.fromInt(newLevel);
            }
        }

        return level;
    }
}