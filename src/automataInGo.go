package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
	"sync"
)

type FiniteTomato struct {
	numStates     int
	initialState  int
	finalStates   []int
	currentStates []int
	graph         [][]string
}

func NewFiniteTomato(numStates int) *FiniteTomato {
	ft := &FiniteTomato{
		numStates:     numStates,
		currentStates: []int{},
		graph:         make([][]string, numStates),
	}
	for i := range ft.graph {
		ft.graph[i] = make([]string, numStates)
	}
	ft.getGraphFromUser()
	ft.getInitialStateFromUser()
	ft.getFinalStatesFromUser()
	ft.currentStates = append(ft.currentStates, ft.initialState)
	return ft
}

func (ft *FiniteTomato) getGraphFromUser() {
	fmt.Println("Enter the graph in the format:(Tab separated values)\n state1 \t state2\t input(as a string)\n Enter -1 to stop\n\n")
	scanner := bufio.NewScanner(os.Stdin)
	for i := 0; i < ft.numStates; i++ {
		for j := 0; j < ft.numStates; j++ {
			ft.graph[i][j] = ""
		}
	}
	for {
		scanner.Scan()
		inputLine := scanner.Text()
		if inputLine == "-1" {
			break
		}
		inputArray := strings.Split(inputLine, "\t")
		state1, _ := strconv.Atoi(inputArray[0])
		state2, _ := strconv.Atoi(inputArray[1])
		input := inputArray[2]
		ft.graph[state1][state2] = input
	}
}

func (ft *FiniteTomato) getInitialStateFromUser() {
	fmt.Println("Enter the initial state:")
	fmt.Scanf("%d", &ft.initialState)
}

func (ft *FiniteTomato) getFinalStatesFromUser() {
	fmt.Println("Enter the final states in the format:(Tab separated values)\n\n\n state1 \t state2\t state3\n\n\n")
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	inputLine := scanner.Text()
	inputArray := strings.Split(inputLine, "\t")
	for _, s := range inputArray {
		if s == "" {
			continue
		}
		state, _ := strconv.Atoi(s)
		ft.finalStates = append(ft.finalStates, state)
	}
}

func (ft *FiniteTomato) printGraph() {
	fmt.Println("The graph is:")
	for i := 0; i < ft.numStates; i++ {
		for j := 0; j < ft.numStates; j++ {
			if ft.graph[i][j] != "" {
				fmt.Printf("%d -> %d : %s\n", i, j, ft.graph[i][j])
			}
		}
	}
}

func (ft *FiniteTomato) inputToMachine(input string, print bool) bool {
	for _, c := range input {
		if len(ft.currentStates) == 0 {
			if print {
				fmt.Printf("%s-> The input is not accepted\n", input)
			}
			ft.currentStates = append(ft.currentStates, ft.initialState)
			return false
		}
		newStates := []int{}
		for _, state := range ft.currentStates {
			for j := 0; j < ft.numStates; j++ {
				if ft.graph[state][j] != "" {
					s := ft.graph[state][j]
					for _, ch := range s {
						if ch == rune(c) {
							newStates = append(newStates, j)
						}
					}
				}
			}
		}
		ft.currentStates = newStates
	}
	if ft.isFinalState() {
		if print {
			fmt.Printf("%s-> is accepted by the machine\n", input)
		}

		ft.currentStates = []int{ft.initialState}
		return true
	} else {
		if print {
			fmt.Printf("%s-> The input is not accepted\n", input)
		}
		ft.currentStates = []int{ft.initialState}
		return false
	}
}

func (ft *FiniteTomato) inputToMachineNFA(input string, print bool) bool {
	var wg sync.WaitGroup
	for _, c := range input {
		if len(ft.currentStates) == 0 {
			if print {
				fmt.Printf("%s-> The input is not accepted\n", input)
			}
			ft.currentStates = append(ft.currentStates, ft.initialState)
			return false
		}
		epsilonPre := []int{}
		wg.Add(len(ft.currentStates))
		newStates := make(chan int)
		for _, state := range ft.currentStates {
			go func(state int) {
				defer wg.Done()
				for j := 0; j < ft.numStates; j++ {
					if ft.graph[state][j] != "" {
						s := ft.graph[state][j]
						for _, ch := range s {
							if ch == 'ε' {
								epsilonPre = append(epsilonPre, j)
							}
						}
					}
				}
			}(state)
		}
		wg.Wait()
		ft.currentStates = epsilonPre
		epsilonPre = []int{}
		wg.Add(len(ft.currentStates))
		for _, state := range ft.currentStates {
			go func(state int) {
				defer wg.Done()
				for j := 0; j < ft.numStates; j++ {
					if ft.graph[state][j] != "" {
						s := ft.graph[state][j]
						for _, ch := range s {
							if ch == rune(c) {
								newStates <- j
							}
						}
					}
				}
			}(state)
		}
		go func() {
			wg.Wait()
			close(newStates)
		}()
		ft.currentStates = []int{}
		for state := range newStates {
			ft.currentStates = append(ft.currentStates, state)
			for j := 0; j < ft.numStates; j++ {
				if ft.graph[state][j] != "" {
					s := ft.graph[state][j]
					for _, ch := range s {
						if ch == 'ε' {
							epsilonPre = append(epsilonPre, j)
						}
					}
				}
			}
		}
		ft.currentStates = append(ft.currentStates, epsilonPre...)
	}
	if ft.isFinalState() {
		if print {
			fmt.Printf("%s-> is accepted by the machine\n", input)
		}
		ft.currentStates = []int{ft.initialState}
		return true
	} else {
		if print {
			fmt.Printf("%s-> The input is not accepted\n", input)
		}
		ft.currentStates = []int{ft.initialState}
		return false
	}
}

func (ft *FiniteTomato) isFinalState() bool {
	if len(ft.currentStates) == 0 {
		return false
	}
	for _, state := range ft.currentStates {
		for _, finalState := range ft.finalStates {
			if state == finalState {
				return true
			}
		}
	}
	return false
}

func fileParser(p string) (*FiniteTomato, error) {
	file, err := os.Open(p)
	if err != nil {
		return nil, fmt.Errorf("File not found: %v", err)
	}
	defer file.Close()

	var graph [][]string
	var initialState int
	finalStates := make([]int, 0)
	passedSize, passedGraph, passedStart, passedFinal := -1, -1, -1, -1

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()
		if len(line) == 0 || line[0] == '#' {
			continue
		}
		if len(line) == 1 {
			size, _ := strconv.Atoi(line)
			graph = make([][]string, size)
			for i := range graph {
				graph[i] = make([]string, size)
			}
			passedSize = 1
			break
		}
	}

	for scanner.Scan() {
		line := scanner.Text()
		if len(line) == 0 || line[0] == '#' {
			continue
		}
		if line[0] == '-' {
			passedGraph = 1
			break
		}
		parts := strings.Split(line, " ")
		if len(parts) == 3 {
			state1, _ := strconv.Atoi(parts[0])
			state2, _ := strconv.Atoi(parts[1])
			graph[state1][state2] = parts[2]
		}
	}

	for i := range graph {
		for j := range graph[i] {
			if i == j {
				if graph[i][j] == "" {
					graph[i][j] = "ε"
				} else if !strings.Contains(graph[i][j], "ε") {
					graph[i][j] += "ε"
				}
			}
		}
	}

	for scanner.Scan() {
		line := scanner.Text()
		if len(line) == 0 || line[0] == '#' {
			continue
		}
		if len(line) == 1 {
			initialState, _ = strconv.Atoi(line)
			passedStart = 1
			break
		}
	}

	for scanner.Scan() {
		line := scanner.Text()
		if len(line) == 0 || line[0] == '#' {
			continue
		}
		if line[0] == '-' {
			break
		}
		parts := strings.Split(line, " ")
		for _, part := range parts {
			state, _ := strconv.Atoi(part)
			finalStates = append(finalStates, state)
		}
		passedFinal = 1
	}

	if passedSize == 1 && passedGraph == 1 && passedStart == 1 && passedFinal == 1 {
		currentStates := []int{initialState}
		return &FiniteTomato{
			numStates:     len(graph),
			initialState:  initialState,
			finalStates:   finalStates,
			currentStates: currentStates,
			graph:         graph,
		}, nil
	}

	return nil, fmt.Errorf("The file is not in the correct format")
}

func (ft *FiniteTomato) assertInput(input string, isSupposedToBeAccepted bool) {
	result := ft.inputToMachine(input, false)
	if result == isSupposedToBeAccepted {
		fmt.Printf("Test Passed for input: %s\n", input)
	} else {
		fmt.Printf("Test Failed for input: %s\n", input)
	}
}

func main() {
	automata, err := fileParser("./src/test2")
	if err != nil {
		fmt.Println(err)
		return
	}
	automata.inputToMachine("abba", true)
	automata.inputToMachine("aabb", true)
	automata.inputToMachine("abbbbbabababa", true)
	automata.inputToMachineNFA("abab", true)
	automata.inputToMachine("abbbb", true)

}
