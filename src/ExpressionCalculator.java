import java.util.Stack;

public class ExpressionCalculator {
    public static int evaluate(String expression){
        String[] tokens = expression.split(" ");

        // Stack for values
        Stack<Integer> values = new Stack<Integer>();

        // Stack for Operators
        Stack<String> operators = new Stack<String>();

        for(int i = 0; i < tokens.length; i++){

            //get char value of token ( 's'in, '*', '+', '/', etc)
            char singleToken = tokens[i].charAt(0);

            // push numbers to value stack
            if(singleToken >= '0' && singleToken <= '9'){
               values.push(Integer.parseInt(tokens[i]));
            }else if (singleToken == '('){
                operators.push(tokens[i]);
            }else if (singleToken == ')'){
             
            }

        }

        return -1;
    }
}
