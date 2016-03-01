A calculator program in Java that evaluates expressions in a very simple integer expression language.
The program takes an input on the command line, computes the result, and prints it to the console. 
For example:
% java calculator.Main "add(2, 2)"
4

Few more examples:
Input	                                             Output
add(1, 2)	                                         3
add(1, mult(2, 3))	                                 7
mult(add(2, 2), div(9, 3))	                         12
let(a, 5, add(a, a))	                             10
let(a, 5, let(b, mult(a, 10), add(b, a)))	         55
let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))) 40

An expression is one of the following:
Numbers: integers between Integer.MIN_VALUE and Integer.MAX_VALUE
Variables: strings of characters, where each character is one of a-z, A-Z
Arithmetic functions: add, sub, mult, div, each taking two arbitrary expressions as arguments.  In other words, each argument may be any of the expressions on this list.
A “let” operator for assigning values to variables:
	let(<variable name>, <value expression>, <expression where variable is used>)
As with arithmetic functions, the value expression and the expression where the variable is used may be an arbitrary expression from this list. 

Following are helpful comments about how to quickly run this project.

Gradle tasks:
To build Jar file:
    gradlew clean build
To compile classes:
    gradlew clean compileJava
To run all unit tests:
    gradlew clean test
To run individual unit test:
    gradlew -Dtest.single=Expression test


To run from command line, build Jar file first with Gradle(see above) then run as following command:
    java -classpath build\libs\assignment_sig1-1.0-SNAPSHOT.jar org.mdeng.assignment_sig.Main -v1 "add(100, -10)"
