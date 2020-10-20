import java.util.Stack;
import java.math.*;

public class ExpressionCalculator {
    public static int evaluate(String expression) {
        String[] tokens = expression.split(" ");

        // Stack for values
        Stack<Double> values = new Stack<Double>();

        // Stack for Operators
        Stack<String> operators = new Stack<String>();

        for (int i = 0; i < tokens.length; i++) {

            //get char value of token ( 's'in, '*', '+', '/', etc)
            char singleToken = tokens[i].charAt(0);

            // push numbers to value stack
            if (singleToken >= '0' && singleToken <= '9') {
                values.push(Double.parseDouble(tokens[i]));
            } else if (singleToken == '(') {
                operators.push(tokens[i]);
            } else if (singleToken == ')') {

            }

        }

        return -1;
    }

    /*** Applies the operator to a and b, in the case of operators (sin, cos,
     *  tan, log10, log, sqrt) operator applied to only a
     *
     * @param operator the operation to be performed on numbers a and b
     * @param a - double - first number to be operated on
     * @param b - double - second number to be operated on
     * @return double - value of ( a <operator> b)
     */
    public static double applyOperator(String operator, double a, double b){
        switch(operator){
            case "sin":
                return Math.sin(a);
            case "cos":
                return Math.cos(a);
            case "tan":
                return Math.tan(a);
            case "log":
                return Math.log10(a);
            case "ln":
                return Math.log(a);
            case "sqrt":
                return Math.sqrt(a);
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0){
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
            }
            return -2;
    }

    /*** Method to check precedence of operators
     *
     * @param op1 - the first operand
     * @param op2 - the second operand
     * @return boolean - true if precedence of 'op2' >= 'op1', false otherwise
     */
    public static boolean hasPrecedence(String op1, String op2){
        if(op2 == "(" || op2 == ")"){
            return false;
        }
        if((op1 == "*" || op1 == "/" || op1 == "sin" || op1 == "cos" ||
                op1 == "tan" || op1 == "sqrt") && (op2 == "+" || op2 == "-")){
            return false;
        }else{
            return true;
        }
    }

}