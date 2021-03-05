'''
Psuedo Code

1. user inputs string
Variables
----------
tokens = validate,clean,split input String



2. Process with Shunting Yard
Variables
----------
stack
queue

while token:
  read token
  if numeric:
    add to queue
  if operator:
    while operator on top of stack with greater precedence:
      pop operators from the stack onto output queue
    push current operator onto stack
  if '(':
    push to stack
  if ')':
    while !'(' on top of stack:
      pop operators from stack to queue
    pop the '(' from stack and trash
  while operator on stack, pop to queue

3. Apply custom eval method on returned queue from 3
4. Return answer


 TO DO
 ---------------------
 Debug:
   - vs + output in certain cases
   Further Input Validation

 Implement:
    command line argument passing
    ??? simple flask api ???

 And:
 Seperate test script


'''


# using only standard libraries
import re,unittest


''' Takes in string and applies a Shunting Yard Algorithm to the expression '''
class Calculate:
    def __init__(self,startString):
        self.startString = startString

    def performCalculations(self):
        tokenList = self.cleanInput(self.startString)
        return self.toRPN(tokenList)

    def cleanInput(self,inString):
        tokens = re.findall("-?\d*\.{0,1}\d+|[-+/*()]", inString)
        return tokens

    def isNumber(self,str):
        try:
            float(str) if '.' in str else int(str)
            return True
        except ValueError:
            return False

    def peekStack(self,stack):
        return stack[-1] if stack else None

    def greaterPrecedence(self,op1, op2):
        ''' Uses Dictionary Mapping to provide comparison '''
        precedences = {'+' : 0, '-' : 0, '*' : 1, '/' : 1}
        return precedences[op1] > precedences[op2]

    def parseRPN(self,expression):
        ops = {
          "+": (lambda a, b: a + b),
          "-": (lambda a, b: a - b),
          "*": (lambda a, b: a * b),
          "/": (lambda a, b: a / b)
        }

        """ Evaluate a reverse polish notation """
        tokens = expression
        stack = []

        for token in tokens:
            if token in ops: #---compare against ops dictionary
                arg2 = stack.pop()
                arg1 = stack.pop()
                result = ops[token](arg1, arg2)
                stack.append(result)

            else:
                try:
                    stack.append(float(token))
                except:
                    stack.append(int(token))

        final = stack.pop()
        if final.is_integer():
            return int(final)
        else:
            return final


    def toRPN(self,tokens):
        ''' Shunting Yard Algorithm '''
        operatorStack = []
        outputQueue = []
        for token in tokens:
            if self.isNumber(token):
                 outputQueue.append(token)
            elif token == '(':
                operatorStack.append(token)
            elif token == ')':
                top = self.peekStack(operatorStack)
                while top != '(':
                    outputQueue.append(operatorStack.pop())
                    top = self.peekStack(operatorStack)
                operatorStack.pop()
            else:
                top = self.peekStack(operatorStack)
                while top is not None and top not in "()" and self.greaterPrecedence(top, token):
                    outputQueue.append(operatorStack.pop())
                operatorStack.append(token)
        while self.peekStack(operatorStack) is not None:
            outputQueue.append(operatorStack.pop())

        out = self.parseRPN(outputQueue)
        return out


class TestProgram(unittest.TestCase):
    def test_positive(self):
        testCases = [["-.32       /.5",-0.64],["4*5/2",10],["-5+-8--11*2",9],["(4-2)*3.5",7]]
        message = "<--- Mismatch --->"
        for x in testCases:
            tmp = Calculate(x[0])
            self.assertEqual(tmp.performCalculations(), x[1], message)


def gatherUserInput():
    userInput = input("Please enter your search string: ")
    return userInput

def main():
    startString = gatherUserInput()
    answer = Calculate(startString)
    print(answer.performCalculations())


if __name__ == "__main__":
    main()
    # unittest.main()
