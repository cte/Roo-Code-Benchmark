#pragma once
#include <stdexcept>

namespace linked_list {

template <typename T>
class List {
private:
    struct Node {
        T data;
        Node* next;
        Node* prev;
        
        Node(const T& value) : data(value), next(nullptr), prev(nullptr) {}
    };
    
    Node* head;
    Node* tail;
    int size;
    
public:
    List() : head(nullptr), tail(nullptr), size(0) {}
    ~List();
    
    void push(const T& value);
    T pop();
    void unshift(const T& value);
    T shift();
    int count() const;
    void erase(const T& value);
};

template <typename T>
List<T>::~List() {
    while (head) {
        Node* temp = head;
        head = head->next;
        delete temp;
    }
}

template <typename T>
void List<T>::push(const T& value) {
    Node* new_node = new Node(value);
    
    if (!head) {
        // List is empty
        head = new_node;
        tail = new_node;
    } else {
        // Add to the end
        new_node->prev = tail;
        tail->next = new_node;
        tail = new_node;
    }
    
    size++;
}

template <typename T>
T List<T>::pop() {
    if (!tail) {
        throw std::runtime_error("Cannot pop from an empty list");
    }
    
    T value = tail->data;
    
    if (head == tail) {
        // Only one element
        delete head;
        head = nullptr;
        tail = nullptr;
    } else {
        // More than one element
        Node* new_tail = tail->prev;
        new_tail->next = nullptr;
        delete tail;
        tail = new_tail;
    }
    
    size--;
    return value;
}

template <typename T>
void List<T>::unshift(const T& value) {
    Node* new_node = new Node(value);
    
    if (!head) {
        // List is empty
        head = new_node;
        tail = new_node;
    } else {
        // Add to the beginning
        new_node->next = head;
        head->prev = new_node;
        head = new_node;
    }
    
    size++;
}

template <typename T>
T List<T>::shift() {
    if (!head) {
        throw std::runtime_error("Cannot shift from an empty list");
    }
    
    T value = head->data;
    
    if (head == tail) {
        // Only one element
        delete head;
        head = nullptr;
        tail = nullptr;
    } else {
        // More than one element
        Node* new_head = head->next;
        new_head->prev = nullptr;
        delete head;
        head = new_head;
    }
    
    size--;
    return value;
}

template <typename T>
int List<T>::count() const {
    return size;
}

template <typename T>
void List<T>::erase(const T& value) {
    Node* current = head;
    
    while (current) {
        if (current->data == value) {
            if (current == head && current == tail) {
                // Only element in the list
                delete current;
                head = nullptr;
                tail = nullptr;
            } else if (current == head) {
                // First element
                head = current->next;
                head->prev = nullptr;
                delete current;
            } else if (current == tail) {
                // Last element
                tail = current->prev;
                tail->next = nullptr;
                delete current;
            } else {
                // Middle element
                current->prev->next = current->next;
                current->next->prev = current->prev;
                delete current;
            }
            
            size--;
            return;
        }
        
        current = current->next;
    }
}

}  // namespace linked_list
