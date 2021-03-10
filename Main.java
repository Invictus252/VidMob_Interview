import java.util.*;
import java.text.*;

public class Main {

    public static void main(String[] args) {
        //-----------------------------------------------------------
        //    UNCOMENT BELOW FOR TESTING
        //-----------------------------------------------------------

//         List<String> testList = new ArrayList<String>();
//         testList.add("1 + 2");
//         testList.add("4*5/2");
//         testList.add("-.32       /.5");
//         testList.add("(4-2)*3.5");
//         testList.add("6-(5-3)+10");
//         testList.add("19 + cinnamon");
//
//         for(String test : testList){
//             Calculator calculate = new Calculator(test.replaceAll("\\s+",""));
//             /**
//              * Check for int or double to give answer in correct format
//              */
//             double answer =  calculate.convertInfixToRPN();
//             DecimalFormat df = new DecimalFormat("0.###");
//             System.out.println(String.format("Answer is :  %s",df.format(answer)));
//           }


        Scanner sc = new Scanner(System.in);
        boolean run = true;
        while (run) {

            System.out.println("Enter equation or exit to end");

            String userInput = sc.nextLine();
            if (userInput.replaceAll("\\s+", "").equalsIgnoreCase("exit")) {
                run = false;
                continue;
            }
            System.out.println("Equation is: " + userInput);
            Calculator calculate = new Calculator(userInput.replaceAll("\\s+", ""));

            double answer = calculate.convertInfixToRPN();
            //Check for int or double to give answer in correct format
            DecimalFormat df = new DecimalFormat("0.###");
            System.out.println(String.format("Answer is :  %s", df.format(answer)));

        }
    }
}

