import java.lang.reflect.Array;
import java.util.NoSuchElementException;

class SimpleLinkedList<T> {
    private Node<T> head;
    private int size;

    // Node class for the linked list
    private static class Node<T> {
        private T data;
        private Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // Default constructor
    SimpleLinkedList() {
        this.head = null;
        this.size = 0;
    }

    // Constructor from array
    SimpleLinkedList(T[] values) {
        this();
        if (values != null) {
            // Add elements in reverse order to maintain LIFO behavior
            for (int i = values.length - 1; i >= 0; i--) {
                push(values[i]);
            }
        }
    }

    // Add element to the front of the list
    void push(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = head;
        head = newNode;
        size++;
    }

    // Remove and return the element from the front of the list
    T pop() {
        if (head == null) {
            throw new NoSuchElementException("Cannot pop from an empty list");
        }
        
        T value = head.data;
        head = head.next;
        size--;
        return value;
    }

    // Reverse the list
    void reverse() {
        if (head == null || head.next == null) {
            return; // Empty list or single element list doesn't need reversal
        }
        
        Node<T> prev = null;
        Node<T> current = head;
        Node<T> next;
        
        while (current != null) {
            next = current.next;  // Store next node
            current.next = prev;  // Reverse the link
            prev = current;       // Move prev to current
            current = next;       // Move current to next
        }
        
        head = prev; // Update head to the new front (previously the last node)
    }

    // Convert list to array
    @SuppressWarnings("unchecked")
    T[] asArray(Class<T> clazz) {
        T[] array = (T[]) Array.newInstance(clazz, size);
        
        Node<T> current = head;
        for (int i = 0; i < size; i++) {
            array[i] = current.data;
            current = current.next;
        }
        
        return array;
    }

    // Return the size of the list
    int size() {
        return size;
    }
}
