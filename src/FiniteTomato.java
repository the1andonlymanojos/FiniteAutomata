import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class FiniteTomato {
    int numberOfStates;
    int initialState;
    int[] finalStates;
    Scanner scanner;

    LinkedList<Integer> currentStates;
    String[][] Graph;

    public FiniteTomato(int numberOfStates, int initialState, int[] finalStates, LinkedList<Integer> currentStates, String[][] graph) {
        this.numberOfStates = numberOfStates;
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.currentStates = currentStates;
        Graph = graph;
    }
    public boolean assertInput(String input, boolean isSupposedToBeAccepted){
        boolean result = inputToMachine(input, false);
        if (result == isSupposedToBeAccepted){
            System.out.println("Test Passed for input: "+input);
            return true;
        }
        else{
            System.out.println("Test Failed for input: "+input);
            return false;
        }
    }

    public FiniteTomato(int numberOfStates) {
        this.numberOfStates = numberOfStates;
        scanner = new Scanner(System.in);
        System.out.println();
        Graph = new String[numberOfStates][numberOfStates];
        currentStates = new LinkedList<>();
        getGraphFromUser();
        getInitialStateFromUser();
        getFinalStatesFromUser();
        currentStates.add(initialState);
    }
    public void getGraphFromUser(){
        System.out.println("Enter the graph in the format:(Tab seperated values)\n state1 \t state2\t input(as a string)\n Enter -1 to stop\n\n");
        for (int i = 0; i < numberOfStates; i++) {
            for (int j = 0; j < numberOfStates; j++) {
                Graph[i][j] = "";
            }
        }
        int state1, state2;
        String input;
        
        while (true) {
            scanner.next();
            String inputLine = scanner.nextLine();
            if (inputLine.equals("-1")) {
                break;
            }
            String[] inputArray = inputLine.split("\t");
            state1 = Integer.parseInt(inputArray[0]);
            state2 = Integer.parseInt(inputArray[1]);
            input = inputArray[2];
            Graph[state1][state2] = input;
        }
       
    }
    public void getInitialStateFromUser(){
        System.out.println("Enter the initial state:");
       
        initialState = scanner.nextInt();

    }
    public void getFinalStatesFromUser(){
        System.out.println("Enter the final states in the format:(Tab seperated values)\n\n\n state1 \t state2\t state3\n\n\n");
        HashSet<Integer> finalStatesSet = new HashSet<>();
        int state;
        String inputLine;
        inputLine = scanner.nextLine();
        String[] inputArray = inputLine.split("\t");
        for (String s : inputArray) {
            if (s.equals("")) {
                continue;
            }
            state = Integer.parseInt(s);
            finalStatesSet.add(state);
        }
        finalStates = new int[finalStatesSet.size()];
        int i = 0;
        for (int state1 : finalStatesSet) {
            finalStates[i] = state1;
            i++;
        }
    }
    public void printGraph(){
        System.out.println("The graph is:");
        for (int i = 0; i < numberOfStates; i++) {
            for (int j = 0; j < numberOfStates; j++) {
                if (Graph[i][j] != "") {
                    System.out.println(i + " -> " + j + " : " + Graph[i][j]);
                }
            }
        }
    }

    public boolean inputToMachine(String input, boolean print) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (currentStates.size() == 0){
                if (print)
                    System.out.println(input+"-> The input is not accepted");
                currentStates.add(initialState);
                return false;
            }
            LinkedList<Integer> newStates = new LinkedList<>();
            for (int state : currentStates) {
              //  System.out.println("checking state: "+state+" for input: "+c);
                for (int j = 0; j < numberOfStates; j++) {
                    if (Graph[state][j]!=null) {
                        String s = Graph[state][j];
                        for (int k = 0; k < s.length(); k++) {
                            if (s.charAt(k) == c) {
                                newStates.add(j);
                            }
                        }
                    }
                }
            }
            currentStates = newStates;
        }
        if(isFinalState()){
            if (print)
                System.out.println(input + "-> is accepted by the machine");
            currentStates.clear();
            currentStates.add(initialState);
            return true;
        }
        else{
            if (print)
                System.out.println(input+"-> The input is not accepted");
            currentStates.clear();
            currentStates.add(initialState);
            return false;
        }
    }
    public boolean inputToMachineNFA(String input, boolean print) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (currentStates.size() == 0){
                if (print)
                System.out.println(input+"-> The input is not accepted");
                currentStates.add(initialState);
                return false;
            }
            LinkedList<Integer> epsilonPre = new LinkedList<>();
            //epsilon closure
            for (int state : currentStates) {
                //  System.out.println("checking state: "+state+" for input: "+c);
                for (int j = 0; j < numberOfStates; j++) {
                    if (Graph[state][j]!=null) {
                        String s = Graph[state][j];
                        for (int k = 0; k < s.length(); k++) {
                            if (s.charAt(k) == 'ε') {
                                epsilonPre.add(j);
                            }
                        }
                    }
                }
            }
            currentStates.clear();
            currentStates.addAll(epsilonPre);
            epsilonPre.clear();
            LinkedList<Integer> newStates = new LinkedList<>();
            for (int state : currentStates) {
                //  System.out.println("checking state: "+state+" for input: "+c);
                for (int j = 0; j < numberOfStates; j++) {
                    if (Graph[state][j]!=null) {
                        String s = Graph[state][j];
                        for (int k = 0; k < s.length(); k++) {
                            if (s.charAt(k) == c) {
                                newStates.add(j);
                            }
                        }
                    }
                }
            }
            for(int state: newStates){
                for (int j = 0; j < numberOfStates; j++) {
                    if (Graph[state][j]!=null) {
                        String s = Graph[state][j];
                        for (int k = 0; k < s.length(); k++) {
                            if (s.charAt(k) == 'ε') {
                                epsilonPre.add(j);
                            }
                        }
                    }
                }
            }
            newStates.clear();
            newStates.addAll(epsilonPre);
            currentStates = newStates;
        }
        if(isFinalState()){
            if (print)
            System.out.println(input + "-> is accepted by the machine");
            currentStates.clear();
            currentStates.add(initialState);
            return true;
        }
        else{
            if (print)
            System.out.println(input+"-> The input is not accepted");
            currentStates.clear();
            currentStates.add(initialState);
            return false;
        }
    }

    public boolean isFinalState() {
        if (currentStates.size() == 0){
            return false;
        }
        for (int state : currentStates) {
            for (int finalState : finalStates) {
                if (state == finalState) {
                    return true;
                }
            }
        }
        return false;
    }


}
