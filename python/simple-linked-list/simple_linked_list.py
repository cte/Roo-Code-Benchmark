class EmptyListException(Exception):
    """Exception raised when trying to access elements of an empty list."""
    pass


class Node:
    def __init__(self, value, next_node=None):
        self._value = value
        self._next = next_node

    def value(self):
        return self._value

    def next(self):
        return self._next


class LinkedList:
    def __init__(self, values=None):
        self._head = None
        self._length = 0
        
        if values is not None:
            for value in values:
                self.push(value)

    def __iter__(self):
        current = self._head
        while current is not None:
            yield current.value()
            current = current.next()

    def __len__(self):
        return self._length

    def head(self):
        if self._head is None:
            raise EmptyListException("The list is empty.")
        return self._head

    def push(self, value):
        self._head = Node(value, self._head)
        self._length += 1

    def pop(self):
        if self._head is None:
            raise EmptyListException("The list is empty.")
        
        value = self._head.value()
        self._head = self._head.next()
        self._length -= 1
        
        return value

    def reversed(self):
        result = LinkedList()
        values = []
        
        # Collect values in the current order
        current = self._head
        while current is not None:
            values.append(current.value())
            current = current.next()
        
        # Add them in reverse order to get the original order when iterated
        for value in values:
            result.push(value)
            
        return result
