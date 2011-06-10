package com.appspot.thinkhea.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

public class Calculator {
	
	public Calculator(){
		operators = new HashMap<String, int[]>();
		// Default support operators
		operators.put("+", new int[] { 0, LEFT_ASSOC });
        operators.put("-", new int[] { 0, LEFT_ASSOC });
        operators.put("*", new int[] { 5, LEFT_ASSOC });
        operators.put("÷", new int[] { 5, LEFT_ASSOC });
        operators.put("%", new int[] { 5, LEFT_ASSOC });
        operators.put("^", new int[] { 10, RIGHT_ASSOC });
	}
	
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;
 
    //List of supported operators
    private Map<String, int[]> operators =null;
	
    /**
     * Test if a certain is an operator .
     * @param token The token to be tested .
     * @return True if token is an operator . Otherwise False .
     */
    private boolean isOperator(String token) {
        return operators.containsKey(token);
    }
    
    /**
     * Test the associativity of a certain operator token .
     * @param token The token to be tested (needs to operator).
     * @param type LEFT_ASSOC or RIGHT_ASSOC
     * @return True if the tokenType equals the input parameter type .
     */
    private boolean isAssociative(String token, int type) {
        if (!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
        if (operators.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    /**
     * Compare precendece of two operators.
     * @param token1 The first operator .
     * @param token2 The second operator .
     * @return A negative number if token1 has a smaller precedence than token2,
     * 0 if the precendences of the two tokens are equal, a positive number
     * otherwise.
     */
    private int cmpPrecedence(String token1, String token2) {
        if (!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalied tokens: " + token1
                    + " " + token2);
        }
        return operators.get(token1)[0] - operators.get(token2)[0];
    }
    
    public String[] infixToRPN(String[] inputTokens) {
        ArrayList<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        // For all the input tokens [S1] read the next token [S2]
        for (String token : inputTokens) {
            if (isOperator(token)) {
                // If token is an operator (x) [S3]
                while (!stack.empty() && isOperator(stack.peek())) {
                    // [S4]
                    if ((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(
                            token, stack.peek()) <= 0)
                            || (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(
                                    token, stack.peek()) < 0)) {
                        out.add(stack.pop());   // [S5] [S6]
                        continue;
                    }
                    break;
                }
                // Push the new operator on the stack [S7]
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);  // [S8]
            } else if (token.equals(")")) {
                // [S9]
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop()); // [S10]
                }
                stack.pop(); // [S11]
            } else {
                out.add(token); // [S12]
            }
        }
        while (!stack.empty()) {
            out.add(stack.pop()); // [S13]
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }
    
	public double process(String s) {
		StringTokenizer st = new StringTokenizer(s);
		Stack<Double> stack = new Stack<Double>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			try {
				stack.push(new Double(token));
			} catch (NumberFormatException e) {
				double v1, v2;
				switch (token.charAt(0)) {
				case '*':
					v2 = ((Double) stack.pop()).doubleValue();
					v1 = ((Double) stack.pop()).doubleValue();
					stack.push(new Double(v1 * v2));
					break;
				case '÷':
					v2 = ((Double) stack.pop()).doubleValue();
					v1 = ((Double) stack.pop()).doubleValue();
					stack.push(new Double(v1 / v2));
					break;
				case '+':
					v2 = ((Double) stack.pop()).doubleValue();
					v1 = ((Double) stack.pop()).doubleValue();
					stack.push(new Double(v1 + v2));
					break;
				case '-':
					v2 = ((Double) stack.pop()).doubleValue();
					v1 = ((Double) stack.pop()).doubleValue();
					stack.push(new Double(v1 - v2));
					break;
				case '%':
					v1 = ((Double) stack.pop()).doubleValue();
					stack.push(new Double(v1 / 100));
					break;
				case '^':
					v2 = ((Double) stack.pop()).doubleValue();
					v1 = ((Double) stack.pop()).doubleValue();
					stack.push(Math.pow(v1 , v2));
					break;
				default:				
				}
			}
		}
		return ((Double) stack.pop()).doubleValue();
	}
    
	public double getResult(String equation) throws Exception{
		String[] array_equation = equation.split(" ");
		String[] rpn_equation = infixToRPN(array_equation);
		String s ="";
        for (String token : rpn_equation) {
            s +=token + "  ";
        }
		return process(s);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Calculator c = new Calculator();
		String s = "1+2";
		double d = c.process(s);
		System.out.println(d);
	}
}
