package main

import (
	"C"
	"fmt"
	"sort"
)

//export Add
// Add retrieves the addition result the two provided values.
func Add(x, y int) int {
	fmt.Printf("[Go]: Adding %v and %v.\r\n", x, y)

	return x + y
}

//export Sort
// Sort the given array in place.
func Sort(arr []int) {
	fmt.Printf("[Go]: Sorting array: %v\r\n", arr)

	sort.Ints(arr)

	fmt.Printf("[Go]: Sorted  array: %v\r\n", arr)
}

func main() {
	fmt.Println("[Go] main.")
}
