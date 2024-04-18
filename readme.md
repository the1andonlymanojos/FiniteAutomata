# Modeling of NFA and DFA in Java and Go

## Introduction

This project is a simple implementation of NFA and DFA in Java and Go. The project is divided into two parts: Java and Go.
The Program takes in a file that describes the FA as a 5 tuple. A FA can be instantiated by using 

`FiniteTomato finiteTomato = fileParser("src/test2");`

where test2 is a simple text file that describes the FA. Below is the format for the file:

```
# DFA for recognizing strings over {a, b} that start with 'abb'
# number of states:
5

# enter transitions as space-separated list,
# Format: current_state next_state input
# Example: 0 1 a means from state 0, on input 'a', transition to state 1

0 0 abε
1 2 a
1 0 b
2 0 a
2 3 b
3 4 b
3 0 a
4 4 abε
-1

# initial state
1

# final state as space-separated list
4
```
Note that anything that begins with a '#' is a comment and will be ignored. The first line is the number of states in the FA. The next lines are the transitions. The last line is the initial state. The last line is the final state. The transitions are in the format `current_state next_state input`. The input can be a single character or `ε` which represents an empty string. The transitions are space-separated. The transitions are read until `-1` is encountered.
`assertInput("abba", true)`
method, where one can provide a string and if the outcome matches the expected outcome as provided in the function call then the test passes.

Also, inputToMachine method can be used to provide a string and the machine will run the string through the FA and return a boolean thats true if accepted, else false.


# What next?

The main goal of this project is to make evaluation of TOC exam papers easier. the developed code can be used to evaluate an NFA, however it needs to be entered as a 5-tuple, which would again be time-consuming and tedious. 

The obvious solution would be to develop a GUI that makes inputting a 5-tuple easier. This would involve creating a GUI that allows the user to input the number of states, the transitions, the initial state, and the final state. The GUI would then generate the 5-tuple and save it to a file. This file can then be used to evaluate the NFA.

But yeah the most effective solution would be to process an image of a FA and convert it to a 5-tuple. This would involve using OCR to read the image and then using a neural network to convert the image to a 5-tuple. This would be a challenging task but would be very useful in the long run.

Currently there are many inneficencies in the code, and the code is not very clean. The code can be optimized and cleaned up to make it more readable and efficient. The code can also be optimized to run faster and use less memory.

Go implementation is better as it uses goroutines for pseudo parralelisim, and is more representative of an NFAs behaviour. The Java implementation is more of a DFA implementation and is not very efficient.