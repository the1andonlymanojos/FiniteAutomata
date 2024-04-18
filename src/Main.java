import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        //initialize w/o file
//        int[] finalStates;
//        finalStates = new int[1];
//        finalStates[0] = 0;
//        String[][] Graph;
//        Graph = new String[3][3];
//        Graph[0][1] = "1";
//        Graph[0][0]="0";
//        Graph[1][0] = "1";
//        Graph[1][2] = "0";
//        Graph[2][2] = "1";
//        Graph[2][1] = "0";
//        LinkedList<Integer> currentStates;
//        currentStates = new LinkedList<>();
//        currentStates.add(0);
//        FiniteTomato finiteTomato = new FiniteTomato(3, 0, finalStates, currentStates, Graph);
//        FiniteTomato finiteTomato = fileParser("src/input");
//        finiteTomato.inputToMachine("0");
//        finiteTomato.inputToMachine("1");
//        finiteTomato.inputToMachine("10");
//        finiteTomato.inputToMachine("11");
//        finiteTomato.inputToMachine("100");
//        finiteTomato.inputToMachine("101");
//        finiteTomato.inputToMachine("110");
//        finiteTomato.inputToMachine("111");
//        finiteTomato.inputToMachine("1000");
//        finiteTomato.inputToMachine("1001");
//        finiteTomato.inputToMachine("1010");
//        finiteTomato.inputToMachine("1011");
//        finiteTomato.inputToMachine("1100");
//        finiteTomato.inputToMachine("1101");
//        finiteTomato.inputToMachine("1110");
//        finiteTomato.inputToMachine("1111");


   //DFA example, init with file. Recognizes strings over {a, b} that start with 'ab'
        // FiniteTomato finiteTomato1 = fileParser("src/test2");
        // finiteTomato1.inputToMachine("ab");
        // finiteTomato1.inputToMachine("aabb");
        // finiteTomato1.inputToMachine("abaaab");
        // finiteTomato1.inputToMachine("aabb");
        // finiteTomato1.inputToMachine("bbbb");
        // finiteTomato1.inputToMachine("aaaa");
        // finiteTomato1.inputToMachine("a");

//        FiniteTomato finiteTomato2 = fileParser("src/test3NFA");
//        finiteTomato2.inputToMachineNFA("101101");
//        finiteTomato2.inputToMachineNFA("101111");
//        finiteTomato2.inputToMachineNFA("10111111111");
//        finiteTomato2.inputToMachineNFA("00000001");

        FiniteTomato finiteTomato = fileParser("src/test2");
        //finiteTomato.inputToMachineNFA("abbababab");
        finiteTomato.assertInput("abb", true);
        finiteTomato.assertInput("ab", true);
        finiteTomato.assertInput("aabb", true);
        finiteTomato.assertInput("abaaab", true);
        finiteTomato.assertInput("aabb", false);
        finiteTomato.assertInput("bbbb", false);

    }

    public static FiniteTomato fileParser(String p){
        File file = new File(p);
        String[][] Graph = null;

        int initialState = 0;
        int[] finStates = null;
        Set<Integer> finalStates = new HashSet<>();
        int passedSize = -1;
        int passedGraph = -1;
        int passedStart = -1;
        int passedFinal = -1;
        try{
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.length() == 0) {
                    continue;
                }
                if (line.charAt(0) == '#') {
                    continue;
                }
                if (line.length()==1) {
                    int size = line.charAt(0) - '0';
                    Graph = new String[size][size];
                    passedSize = 1;
                    break;
                }

            }
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                if (line.length() == 0) {
                    continue;
                }
                if (line.charAt(0) == '#') {
                    continue;
                }
                if (line.charAt(0)=='-') {
                    passedGraph = 1;
                    break;
                }
                String[] parts = line.split(" ");
                if (parts.length==3) {
                    Graph[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])] = parts[2];
                }
            }
            for (int i = 0; i < Graph.length; i++) {
                for (int j = 0; j < Graph[i].length; j++) {
                    if (i==j) {
                        if (Graph[i][j]==null) {
                            Graph[i][j] = "ε";
                        }
                        else{
                            if (!Graph[i][j].contains("ε")) {
                                Graph[i][j] = Graph[i][j]+"ε";
                            }
                        }
                        if (!Graph[i][j].contains("ε")) {
                            
                            Graph[i][j] = Graph[i][j]+"ε";
                        }                        
                    }
                }
            }
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.length() == 0) {
                    continue;
                }
                if (line.charAt(0) == '#') {
                    continue;
                }
                if (line.length()==1) {
                    int init = line.charAt(0) - '0';
                    initialState = init;
                    passedStart = 1;
                    break;
                }

            }
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                if (line.length() == 0) {
                    continue;
                }
                if (line.charAt(0) == '#') {
                    continue;
                }
                if (line.charAt(0)=='-') {
                    break;
                }
                String[] parts = line.split(" ");
                for (String part : parts) {
                    finalStates.add(Integer.parseInt(part));
                }
                finStates = new int[finalStates.size()];
                int i = 0;
                for (int state : finalStates) {
                    finStates[i] = state;
                    i++;
                }
                passedFinal = 1;
            }
            System.out.println("DEBUG ADJ MATRIX");
            System.out.println("----------------");
            for (int i = 0; i < Graph.length; i++) {
                System.out.print(" "+i);
            }
            for (int i = 0; i < Graph.length; i++) {
                System.out.print("\n"+i);
                for (int j = 0; j < Graph[i].length; j++) {
                    System.out.print(" "+Graph[i][j]);
                }
            }
            System.out.println();
            System.out.println("----------------");
            System.out.println();
            System.out.println();
            System.out.println();
            if (passedSize==1 && passedGraph==1 && passedStart==1 && passedFinal==1) {
                LinkedList currentStates = new LinkedList<>();
                currentStates.add(initialState);
                FiniteTomato finiteTomato = new FiniteTomato(Graph.length, initialState, finStates, currentStates, Graph);
                return finiteTomato;
            }
            else{
                System.out.println("The file is not in the correct format");
                return null;
            }
        }catch(Exception e){
            System.out.println("File not found");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return null;
        }
    }


}
