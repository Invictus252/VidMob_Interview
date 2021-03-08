package com.company;
import java.util.*;
import java.text.*;

/**
 * infixToRpn.java
 * Retrieves user input for infix notation. Converts to postfix(Reverse Polish Notation) and evaluates the
 * answer accordingly.
 * @author Jason Smith
 * @date 3/8/2021
 */
public class infixToRpn{
    /**
     * @param str String of possible number
     * @return True is number / False is not
     */
     static boolean isNumber(String str) {
          try{
            Double.valueOf(str);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    /**
     * Converts User input to Reverse Polish Notation
     * @param infixNotation User input string
     * @return Queue of operators and operands in postfix order
     */
    Queue<String> convertInfixToRPN(String infixNotation) {
        String[] input = infixNotation.split("(?<=[-+*/\\(\\)])|(?=[-+*/\\(\\)])");
        Integer operatorCount = infixNotation.replaceAll("[^-+/*]","").length();
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("/", 5);
        precedence.put("*", 5);
        precedence.put("+", 4);
        precedence.put("-", 4);
        precedence.put("(", 0);

        Queue<String> outputQueue = new LinkedList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : input) {
            token = token.replaceAll("\\s+","");
            if ("(".equals(token)) {
                operatorStack.push(token);
                continue;
            }

            if (")".equals(token)) {
                while (!"(".equals(operatorStack.peek())) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.pop();
                continue;
            }
            // an operator
            if (precedence.containsKey(token)) {
                while (!operatorStack.empty() && precedence.get(token) <= precedence.get(operatorStack.peek())) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.push(token);
                continue;
            }

            if (isNumber(token)) {
                outputQueue.add(token);
                continue;
            }
            if(operatorStack.size() < outputQueue.size() ){
                throw new IllegalArgumentException(String.format("Syntax Error in %s",infixNotation));
            }

            throw new IllegalArgumentException(String.format("Invalid Equation String Entered:  %s",infixNotation));
        }


        // at the end, pop all the elements in operatorStack to outputQueue
        while (!operatorStack.isEmpty()) {
            if(operatorStack.size() > outputQueue.size() ){
                throw new IllegalArgumentException(String.format("Syntax Error in %s",infixNotation));
            }
            outputQueue.add(operatorStack.pop());
        }

        return outputQueue;
    }

    /**
     * Evaluates Reverse Polish Notation and performs needed calculations
     * @param tokens String[] of postfix tokens to process
     * @return double of correct answer
     */
    public double evaluateRpn(String[] tokens){
        Stack<Double> outputStack = new Stack<>();
        double rightOperand,leftOperand;
        for(int i = 0; i < tokens.length; i++){
            switch (tokens[i]){
                case "+":
                    if(outputStack.size() == 1){
                        throw new IllegalArgumentException("Invalid amount of operands to operators");
                    }
                    outputStack.push(outputStack.pop() + outputStack.pop());
                    break;
                case "-":
                    if( outputStack.size() == 1){
                        rightOperand = outputStack.pop();
                        outputStack.push(rightOperand * -1);
                        break;
                    }
                    else{
                        rightOperand = outputStack.pop();
                        leftOperand = outputStack.pop();
                        outputStack.push(leftOperand - rightOperand);
                        break;
                    }
                case "*":
                    if(outputStack.size() == 1){
                        throw new IllegalArgumentException("Invalid amount of operands to operators");
                    }
                    outputStack.push(outputStack.pop() * outputStack.pop());
                    break;
                case "/":
                    if(outputStack.size() == 1){
                        throw new IllegalArgumentException("Invalid amount of operands to operators");
                    }
                    rightOperand = outputStack.pop();
                    if(rightOperand != 0){
                        leftOperand = outputStack.pop();
                        outputStack.push(leftOperand / rightOperand);
                    }
                    break;
                default:
                    outputStack.push(Double.parseDouble(tokens[i]));
            }
        }
        return outputStack.pop();
    }

    public String[] toStringArray(Queue in) {
        var list = new ArrayList(in);
        String out[] = new String[list.size()];// ArrayList to String Array conversion
        for(int j =0;j<list.size();j++){
            out[j] = (String) list.get(j);
        }
        return out;
    }

    public static void main(String[] args) {

        /**
         * Uncomment Below for testing
         */
//        String testCases[] = {"1 + 2", "4*5/2", "-.32       /.5", "(4-2)*3.5","6-(5-3)+10","19 + cinnamon"};

//        for(int j =0;j<testCases.length;j++){
//            Queue<String> toRpn = new infixToRpn().convertInfixToRPN(testCases[j]);
//            var rpnArray = new infixToRpn().toStringArray(toRpn);
//            for(int i = 0;i < rpnArray.length;i++){
//                System.out.print(rpnArray[i] + " ");
//            }
//            double answer = new infixToRpn().evaluateRpn(rpnArray);
//            DecimalFormat df = new DecimalFormat("0.###");
//            System.out.println(String.format("Answer is :  %s",df.format(answer)));
//        }
        //------------------------------------------------------------------------------------------------------



        var run = true;
        while(run){
            Scanner appScanner = new Scanner(System.in);
            System.out.println("[use 'exit' to end]\n  Enter equation:");

            String userInput = appScanner.nextLine();  // Read user input
            if(userInput.replaceAll("\\s+","").equalsIgnoreCase("exit")){
                run = false;
                continue;
            }
            Queue<String> toRpn = new infixToRpn().convertInfixToRPN(userInput);
            var rpnArray = new infixToRpn().toStringArray(toRpn);
            for(int i = 0;i < rpnArray.length;i++){
                System.out.print(rpnArray[i] + " ");
            }
            double answer = new infixToRpn().evaluateRpn(rpnArray);
            /**
             * Check for int or double to give answer in correct format
             */
            DecimalFormat df = new DecimalFormat("0.###");
            System.out.println(String.format("Answer is :  %s",df.format(answer)));

        }


    }
}

