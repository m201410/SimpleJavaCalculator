package org.mdeng.assignment_sig;

import org.junit.Test;

import static org.junit.Assert.*;

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

public class MainTest {

    @Test
    public void testMain() throws Exception {
        String[] args1 = {"let(a, 5, add(a, a))"};
        Main.main(args1);
        assertEquals(Main.mCurrentLevel, LogUtil.VerbosityLevel.ERROR); // default
        assert(Main.mExpressionString.equals(args1[0]));

        String[] args2 = {"-v1", "let(a, 5, add(a, a))"};
        Main.main(args2);
        assertEquals(Main.mCurrentLevel, LogUtil.VerbosityLevel.INFO);
        assert(Main.mExpressionString.equals(args2[1]));
    }
}