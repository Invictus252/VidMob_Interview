# VidMob_Interview
(IN PROGRESS) Interview 3 Task: Write a calculator program. The program should let a user enter a math problem as a string and get an answer.

This a python file using only builtin libraries.

Can be run with <code>python3 vidmobCalculator.py</code>

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
