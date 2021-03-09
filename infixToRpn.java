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
    private static boolean isNumber(String str) {
          try{
            Double.valueOf(str);
            return true;
        } catch(Exception e){
            return false;
        }
    }

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
     * Converts User input to Reverse Polish Notation
     * @param infixNotation User input string
     * @return Queue of operators and operands in postfix order
     */
    public Queue<String> convertInfixToRPN(String infixNotation) {
        if(!isMatchingParenthesis(infixNotation)){
          throw new IllegalArgumentException(String.format("Mismatched () in %s",infixNotation));
        }
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

    public List<String> toStringList(Queue<String> in) {
        ArrayList<String> list = new ArrayList<String>(in);
        List<String> outList = new ArrayList<String>();
        for(int j =0;j<list.size();j++){
            outList.add(list.get(j));
        }
        return outList;
    }

    public static void main(String[] args) {
        // List<String> testList = new ArrayList<String>();
        // testList.add("1 + 2");
        // testList.add("4*5/2");
        // testList.add("-.32       /.5");
        // testList.add("(4-2)*3.5");
        // testList.add("6-(5-3)+10");
        // testList.add("19 + cinnamon");
        //
        // for(String test : testList){
        //     Queue<String> toRpn = new infixToRpn().convertInfixToRPN(test);
        //     List<String> rpnArray = new infixToRpn().toStringList(toRpn);
        //     for(String answer : rpnArray){
        //         System.out.print(answer + " ");
        //     }
        //     double answer = new infixToRpn().evaluateRpn(rpnArray);
        //     /**
        //      * Check for int or double to give answer in correct format
        //      */
        //     DecimalFormat df = new DecimalFormat("0.###");
        //     System.out.println(String.format("Answer is :  %s",df.format(answer)));
        //
        //
        //
        //     }
        //
        //
        // }

        Scanner sc = new Scanner(System.in);
        boolean run = true;
        while(run){
          System.out.println("Enter equation or exit to end");

          String userInput = sc.nextLine();
          if(userInput.replaceAll("\\s+","").equalsIgnoreCase("exit")){
            run = false;
            continue;
          }
          System.out.println("Equation is: " + userInput);
          Queue<String> toRpn = new infixToRpn().convertInfixToRPN(userInput.replaceAll("\\s+",""));
          List<String> rpnArray = new infixToRpn().toStringList(toRpn);
          for(String answer : rpnArray){
              System.out.print(answer + " ");
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
