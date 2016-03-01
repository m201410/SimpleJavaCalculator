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
 */

public class LogUtil {
    public enum VerbosityLevel {
        ERROR(0), // Used for catching internal program error
        INFO(1),  // Used for helping user to understand output
        DEBUG(2),
        INVALID(3);

        private int value;

        VerbosityLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static VerbosityLevel fromInt(int id)
        {
            VerbosityLevel[] As = VerbosityLevel.values();
            for(int i = 0; i < As.length; i++)
            {
                if(As[i].getValue() == id)
                    return As[i];
            }
            return VerbosityLevel.INVALID;
        }

        public static boolean isValid(int i) {
            return (i == ERROR.getValue() || i == INFO.getValue() || i == DEBUG.getValue());
        }
    };

    // Default to be Error level.
    private static VerbosityLevel currentLevel = VerbosityLevel.ERROR;

    private static final String LOG_KEYWORD = "Calculator";

    public static VerbosityLevel getVerbosityLevel() {
        return currentLevel;
    }

    public static void setVerbosityLevel(VerbosityLevel level) {
        currentLevel = level;
    }

    public static void log(VerbosityLevel level, String str) {
        if (level.getValue() <= currentLevel.getValue()) {
            System.err.printf("%s(%d): %s\n", LOG_KEYWORD, level.getValue(), str);
        }
    }
}
