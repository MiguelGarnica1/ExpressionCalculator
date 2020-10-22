import java.util.Stack;
import java.math.*;

public class ExpressionCalculator {

    public static void main(String[] args){
        String addition = "2 + 3 + 2";
        System.out.println(addition + " = " + evaluate(addition));

        String subtraction = "10 - 3 + 2";
        System.out.println(subtraction + " = " + evaluate(subtraction));

        String multiExp = "2 + 3 * 3";
        System.out.println(multiExp + " = " + evaluate(multiExp));

        String pemdas = "1 + ( 2 + 3 ) * 3";
        System.out.println(pemdas + " = " + evaluate(pemdas));

        String decimals = "109.45 + 311.24";
        System.out.println(decimals + " = " + evaluate(decimals));

        String simpTrig = "cos ( 3.145926 )";
        System.out.println(simpTrig + " = " + evaluate(simpTrig));

        String complexTrig = "sin ( 3.145926 / 2 ) + cos ( 3.145926 )";
        System.out.println(complexTrig + " = " + evaluate(complexTrig));

        String exponents = "2 ^ ( 3 + 1 )";
        System.out.println(exponents + " = " + evaluate(exponents));

        String unaryOP = "5 + 3 + -2";
        System.out.println(unaryOP + " = " + evaluate(unaryOP));

        String unaryComplex = "2 * -( 3 + 1 )";
        System.out.println(unaryComplex + " = " + evaluate(unaryComplex));

        String unaryTrig = "1 + -sin ( 3.14 / 2 )";
        System.out.println(unaryTrig + " = " + evaluate(unaryTrig));

        String bigExpression = "-5.78 + -( 4 - 2.23 ) + sin ( 0 ) * ( cos ( 1 ) / ( 1 + tan ( 2 * ln ( -3 + 2 * ( 1.23 + 99.111 ) ) ) ) )";
        System.out.println(bigExpression + " = " + evaluate(bigExpression));
    }

    /*** evaluate a mathematical expression in infix format to postfix
     *
     * @param expression - string - mathematical expression to be evaluated
     * @return double - the result of evaluating the mathematical expression
     */
    public static double evaluate(String expression) {
        String[] tokens = expression.split(" ");

        // Stack for values
        Stack<Double> values = new Stack<Double>();

        // Stack for Operators
        Stack<String> operators = new Stack<String>();

        for (int i = 0; i < tokens.length; i++) {

            // skip any extra whitespaces
            if(tokens[i].equals(" ")) continue;

            String currentToken = tokens[i];

            //get char value of token ( 's'in, '*', '+', '/', etc)
            char singleToken = tokens[i].charAt(0);

            // check for unary operation of "-"
            if(singleToken == '-' && tokens[i].length() > 1){
                singleToken = tokens[i].charAt(1); // move to next token
                if(!(singleToken >= '0' &&  singleToken <= '9')){
                    // if it is not a number add -1 and * to stacks
                    values.push(-1.0);
                    operators.push("*");
                    tokens[i] = tokens[i].replace("-","");
                }
            }

            // push numbers to value stack
            if (singleToken >= '0' && singleToken <= '9') {
                values.push(Double.parseDouble(tokens[i]));
            } else if (singleToken == '(') {
                operators.push(tokens[i]);
            } else if (singleToken == ')') {
                while(!operators.peek().equals("(")){

                    // check if operator is regular or trig and calculate accordingly
                    double operatorResult = 0;
                    if(isOperator(operators.peek())){
                        operatorResult = applyOperatorToA_B(operators.pop(),
                                values.pop(), values.pop());
                    } else if (isSpecialOperator(operators.peek())){
                        operatorResult = applyOperatorToX(operators.pop(), values.pop());
                    }
                    values.push(operatorResult);
                }
                operators.pop();
            }
            else if(isOperator(tokens[i]) || isSpecialOperator(tokens[i])){ // check for operator
                // while top of operator stack has same
                // or greater precedence to this operator
                // apply operator to top two elements in value stack
                while(!operators.isEmpty() &&
                        hasPrecedence(tokens[i], operators.peek())){

                    // check if operator is regular or trig and calculate accordingly
                    double operatorResult = 0;
                    if(isOperator(operators.peek())){
                        operatorResult = applyOperatorToA_B(operators.pop(),
                                values.pop(), values.pop());
                    } else if (isSpecialOperator(operators.peek())){
                        operatorResult = applyOperatorToX(operators.pop(), values.pop());
                    }
                    values.push(operatorResult);
                }
                // push current token to operator stack
                operators.push(tokens[i]);
            }
        }

        // at this point expression should be parsed int post fix
        // calculate expression
        while(!operators.isEmpty()){

            // check if operator is regular or trig and calculate accordingly
            double operatorResult = 0;
            if(isOperator(operators.peek())){
                operatorResult = applyOperatorToA_B(operators.pop(),
                        values.pop(), values.pop());
            } else if (isSpecialOperator(operators.peek())){
                operatorResult = applyOperatorToX(operators.pop(), values.pop());
            }
            values.push(operatorResult);
        }

        // first element in value stack should contain result
        return values.pop();

    }

    /*** In the case of operators (sin, cos,
     *  tan, log10, log, sqrt) operator applied to only x
     *
     * @param operator - the operation to be performed on number x
     * @param x - number to be operated
     * @return double - result of operand applied
     */
    public static double applyOperatorToX(String operator, double x){
        switch(operator) {
            case "sin":
            case "-sin":
                return Math.sin(x);
            case "cos":
            case "-cos":
                return Math.cos(x);
            case "tan":
            case "-tan":
                return Math.tan(x);
            case "log":
            case "-log":
                return Math.log10(x);
            case "ln":
            case "-ln":
                return Math.log(x);
            case "sqrt":
            case "-sqrt":
                return Math.sqrt(x);
            case "cot":
            case "-cot":
                return  1.0 / Math.tan(x);
        }
        return -3;
    }

    /*** Applies the operator to a and b
     *
     * @param operator the operation to be performed on numbers a and b
     * @param a - double - first number to be operated on
     * @param b - double - second number to be operated on
     * @return double - value of ( a <operator> b)
     */
    public static double applyOperatorToA_B(String operator, double a, double b){
        switch(operator){
            case "^":
                return Math.pow(b, a);
            case "+":
                return a + b;
            case "-":
                return b - a;
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
        if(op2.equals("(") || op2.equals(")")){
            return false;
        }
        if((op1.equals("*") || op1.equals("/") || op1.equals("sin") || op1.equals("cos") ||
                op1.equals("tan") || op1.equals("log") || op1.equals("ln") || op1.equals("sqrt")
                || op1.equals("cot") || op1.equals("^"))
                && (op2.equals("+") || op2.equals("-"))){
            return false;
        }else{
            return true;
        }
    }

    /*** Checks if string is a mathematical operator
     *
     * @param str - potential operator to be checked
     * @return boolean - true if str is an operator, false if otherwise
     */
    public static boolean isOperator(String str){
        if(str.equals("*") || str.equals("/") ||
                str.equals("+") ||str.equals("-") ||str.equals("^")){
            return true;
        }
        return false;
    }

    /*** Checks if string is a special operator that takes one input (sin, cos,
     *  tan, log10, ln, sqrt)
     *
     * @param str - potential operator to be checked
     * @return boolean - true if str is an operator, false if otherwise
     */
    public static boolean isSpecialOperator(String str){
        if(str.equals("sin") || str.equals("cos") || str.equals("tan") || str.equals("log") ||
                str.equals("ln") || str.equals("sqrt") || str.equals("cot")){
            return true;
        }
        return false;
    }


}