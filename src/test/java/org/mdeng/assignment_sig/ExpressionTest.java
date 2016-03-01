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

import org.junit.Test;

import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testParse() throws Exception {
        Expression e = new Expression("add(1, 2)");
        assertEquals(e.parse(), 3);

        e = new Expression("mult(-5, 2)");
        assertEquals(e.parse(), -10);

        e = new Expression("add(1, mult(2, 3))");
        assertEquals(e.parse(), 7);

        e = new Expression("mult(add(2, 2), div(9, 3))");
        assertEquals(e.parse(), 12);

        e = new Expression("let(a, 5, add(a, a))");
        assertEquals(e.parse(), 10);

        e = new Expression("let(a, 5, let(b, mult(a, 10), add(b, a)))");
        assertEquals(e.parse(), 55);

        e = new Expression("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))");
        assertEquals(e.parse(), 40);
    }

    @Test
    public void testParseException() throws Exception {
        Expression e;
        // Should throw exception when variable is not defined.
        try {
            e = new Expression("add(1, mult(a, 3))");
            e.parse();
            fail("Should throw exception showing invalid variable name");
        } catch (RuntimeException ex) {
        }

        // Should throw exception when running into invalid variable name.
        try {
            e = new Expression("mult(add(2, x56), div(9, 3))");
            e.parse();
            fail("Should throw exception showing invalid variable name");
        } catch (RuntimeException ex) {
        }

        // Should throw exception when running into invalid integer number(larger than Integer.MAX_VALUE.
        try {
            e = new Expression("let(a, 5, add(a, 2147483648))");
            e.parse();
            fail("Should throw exception showing invalid number format");
        } catch (RuntimeException ex) {
        }

        // Should throw exception when the second argument of let() can't be evaluated to be a value.
        try {
            e = new Expression("let(a, ccc, let(b, mult(a, 10), add(b, a)))");
            e.parse();
            fail("Should throw exception showing invalid variable name");
        } catch (RuntimeException ex) {
        }

    }
}