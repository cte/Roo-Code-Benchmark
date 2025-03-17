package linkedlist

import (
	"errors"
)

// Element represents a node in the linked list
type Element struct {
	value int
	next  *Element
}

// List represents a singly linked list with a head pointer and size
type List struct {
	head *Element
	size int
}

// New creates a new linked list from a slice of integers
func New(elements []int) *List {
	list := &List{}
	
	if elements == nil || len(elements) == 0 {
		return list
	}
	
	// Add each element to the list
	for _, val := range elements {
		list.Push(val)
	}
	
	return list
}

// Size returns the number of elements in the list
func (l *List) Size() int {
	return l.size
}

// Push adds an element to the end of the list
func (l *List) Push(element int) {
	newElement := &Element{
		value: element,
		next:  nil,
	}
	
	// If the list is empty, set the head to the new element
	if l.head == nil {
		l.head = newElement
	} else {
		// Otherwise, traverse to the end of the list and add the new element
		current := l.head
		for current.next != nil {
			current = current.next
		}
		current.next = newElement
	}
	
	l.size++
}

// Pop removes and returns the last element from the list
func (l *List) Pop() (int, error) {
	if l.head == nil {
		return 0, errors.New("list is empty")
	}
	
	// If there's only one element, remove it and return its value
	if l.head.next == nil {
		value := l.head.value
		l.head = nil
		l.size--
		return value, nil
	}
	
	// Otherwise, traverse to the second-to-last element
	current := l.head
	for current.next.next != nil {
		current = current.next
	}
	
	// Get the value of the last element
	value := current.next.value
	
	// Remove the last element
	current.next = nil
	l.size--
	
	return value, nil
}

// Array converts the list to a slice of integers
func (l *List) Array() []int {
	if l.head == nil {
		return []int{}
	}
	
	result := make([]int, 0, l.size)
	current := l.head
	
	// Traverse the list and add each value to the result
	for current != nil {
		result = append(result, current.value)
		current = current.next
	}
	
	return result
}

// Reverse returns a new list with the elements in reverse order
func (l *List) Reverse() *List {
	if l.head == nil {
		return New([]int{})
	}
	
	// Convert the list to an array
	arr := l.Array()
	
	// Reverse the array
	for i, j := 0, len(arr)-1; i < j; i, j = i+1, j-1 {
		arr[i], arr[j] = arr[j], arr[i]
	}
	
	// Create a new list from the reversed array
	return New(arr)
}
