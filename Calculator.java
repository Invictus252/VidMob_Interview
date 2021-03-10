import java.util.*;

/**
 * Calculator.java
 * Retrieves user input for infix notation. Converts to postfix(Reverse Polish Notation) and evaluates the
 * answer accordingly.
 * @author Jason Smith
 * @date 3/8/2021
 */
public class Calculator{
    private final String initialInput;

    /**
     * Constructor
     * @param equation Input from user
     */
    public Calculator(String equation){
        initialInput = equation;
    }

    /**
     * isNumber
     * @param str String of possible number
     * @return True is number / False is not
     */
    private static boolean isNumber(String str) {
          try{
            Double.valueOf(str);
            return true;
        } catch(Exception e){
            return false;
        }
    }


    /**
     * isMatchingParenthesis
    * @param Input string
    * @return Boolean True if matched
    **/
    private static boolean isMatchingParenthesis(String str){
      int count = 0;
      char[] characters = str.toCharArray();
      for(char ch : characters){
        if(ch == '(')
          count++;
        else if(ch == ')'){
          count--;
          if(count<0)
            return false;
        }
      }
      if(count==0)
        return true;
      else
        return false;
    }


    /**
     * Converts User input to Reverse Polish Notation and
     * passes it through solving in evaluateRpn
     * @return double of the answer
     */
    public double convertInfixToRPN() {
        if(!isMatchingParenthesis(initialInput)){
          throw new IllegalArgumentException(String.format("Mismatched () in %s",initialInput));
        }
        String[] input = initialInput.split("(?<=[-+*/\\(\\)])|(?=[-+*/\\(\\)])");
        Integer operatorCount = initialInput.replaceAll("[^-+/*]","").length();
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
                throw new IllegalArgumentException(String.format("Syntax Error in %s",initialInput));
            }

            throw new IllegalArgumentException(String.format("Invalid Equation String Entered:  %s",initialInput));
        }


        // at the end, pop all the elements in operatorStack to outputQueue
        while (!operatorStack.isEmpty()) {
            if(operatorStack.size() > outputQueue.size() ){
                throw new IllegalArgumentException(String.format("Syntax Error in %s",initialInput));
            }
            outputQueue.add(operatorStack.pop());
        }

        return evaluateRpn(toStringList(outputQueue));
    }

    /**
     * Evaluates Reverse Polish Notation and performs needed calculations
     * @param tokens String[] of postfix tokens to process
     * @return double of correct answer
     */
    public double evaluateRpn(List<String> tokens){
        Stack<Double> outputStack = new Stack<>();
        double rightOperand,leftOperand;
        for(String token : tokens){
            switch (token){
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
                    outputStack.push(Double.parseDouble(token));
            }
        }
        return outputStack.pop();
    }

    private List<String> toStringList(Queue<String> in) {
        ArrayList<String> list = new ArrayList<String>(in);
        List<String> outList = new ArrayList<String>();
        for(int j =0;j<list.size();j++){
            outList.add(list.get(j));
        }
        return outList;
    }

    }
