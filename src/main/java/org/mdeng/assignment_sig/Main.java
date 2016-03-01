package org.mdeng.assignment_sig;

/**
 * Created by dengm_000 on 2/29/2016.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Got following arguments:");
        for (String s: args) {
            System.out.println(s);
        }
        if (args.length != 1) {
            throw new RuntimeException("Can only accept one argument as expression!");
        }

        Expression expression = new Expression(args[0]);
        int value = expression.parse();
        System.out.println("Resule: " + value);
    }
}
