// this module adds some functionality based on the required implementations
// here like: `LinkedList::pop_back` or `Clone for LinkedList<T>`
// You are free to use anything in it, but it's mainly for the test framework.
mod pre_implemented;

use std::marker::PhantomData;
use std::ptr::NonNull;

/// A node in our doubly linked list.
///
/// Each node contains the actual data element and pointers to the next and previous nodes.
struct Node<T> {
    /// The actual data stored in the node
    element: T,
    /// Pointer to the next node
    next: Option<NonNull<Node<T>>>,
    /// Pointer to the previous node
    prev: Option<NonNull<Node<T>>>,
}

impl<T> Node<T> {
    /// Create a new node with the given element
    fn new(element: T) -> Self {
        Node {
            element,
            next: None,
            prev: None,
        }
    }

    /// Convert a node into a raw pointer
    ///
    /// # Safety
    ///
    /// This is safe because Box guarantees the pointer is non-null.
    fn into_ptr(node: Box<Self>) -> NonNull<Self> {
        // Safety: Box guarantees the pointer is non-null
        unsafe { NonNull::new_unchecked(Box::into_raw(node)) }
    }
}

/// A doubly linked list implementation using unsafe Rust.
///
/// # Safety Invariants
///
/// The following invariants are maintained at all times:
///
/// 1. If the list is empty, both `head` and `tail` are `None`.
/// 2. If the list is non-empty, both `head` and `tail` are `Some` and point to valid nodes.
/// 3. For any node in the list:
///    - If `next` is `Some`, it points to a valid node whose `prev` points back to this node.
///    - If `prev` is `Some`, it points to a valid node whose `next` points back to this node.
/// 4. The `head` node has `prev` set to `None`.
/// 5. The `tail` node has `next` set to `None`.
/// 6. The `length` field accurately reflects the number of nodes in the list.
///
/// These invariants ensure memory safety and prevent issues like use-after-free,
/// double-free, and dangling pointers.
pub struct LinkedList<T> {
    /// Pointer to the first node
    head: Option<NonNull<Node<T>>>,
    /// Pointer to the last node
    tail: Option<NonNull<Node<T>>>,
    /// Number of elements in the list
    length: usize,
    /// PhantomData to indicate that LinkedList<T> owns values of type T
    /// This is needed for correct variance behavior
    _marker: PhantomData<T>,
}

/// A cursor for navigating and modifying a LinkedList.
///
/// The cursor is positioned at a specific node in the list, or at the beginning or end
/// if the list is empty. It provides methods to move forward and backward through the list,
/// and to insert or remove elements at the current position.
pub struct Cursor<'a, T> {
    /// Reference to the list
    list: &'a mut LinkedList<T>,
    /// Current node the cursor is pointing to
    current: Option<NonNull<Node<T>>>,
    /// Index of the current node (for efficient length tracking)
    index: usize,
}

/// An iterator over the elements of a LinkedList.
///
/// This iterator traverses the list from front to back, yielding immutable
/// references to each element.
pub struct Iter<'a, T> {
    /// Current node the iterator is pointing to
    current: Option<NonNull<Node<T>>>,
    /// Number of elements left to yield
    remaining: usize,
    /// PhantomData to indicate that Iter<'a, T> borrows values of type &'a T
    /// This is needed for correct variance behavior
    _marker: PhantomData<&'a T>,
}

impl<T> LinkedList<T> {
    pub fn new() -> Self {
        LinkedList {
            head: None,
            tail: None,
            length: 0,
            _marker: PhantomData,
        }
    }

    // You may be wondering why it's necessary to have is_empty()
    // when it can easily be determined from len().
    // It's good custom to have both because len() can be expensive for some types,
    // whereas is_empty() is almost always cheap.
    // (Also ask yourself whether len() is expensive for LinkedList)
    pub fn is_empty(&self) -> bool {
        self.length == 0
    }

    pub fn len(&self) -> usize {
        self.length
    }

    /// Return a cursor positioned on the front element
    pub fn cursor_front(&mut self) -> Cursor<'_, T> {
        Cursor {
            current: self.head,
            list: self,
            index: 0,
        }
    }

    /// Return a cursor positioned on the back element
    pub fn cursor_back(&mut self) -> Cursor<'_, T> {
        let index = if self.is_empty() { 0 } else { self.length - 1 };
        Cursor {
            current: self.tail,
            list: self,
            index,
        }
    }

    /// Return an iterator that moves from front to back
    pub fn iter(&self) -> Iter<'_, T> {
        Iter {
            current: self.head,
            remaining: self.length,
            _marker: PhantomData,
        }
    }
}

/// The cursor is expected to act as if it is at the position of an element
/// and it also has to work with and be able to insert into an empty list.
impl<T> Cursor<'_, T> {
    /// Takes a mutable reference to the current element.
    ///
    /// Returns `None` if the cursor is not pointing to any element.
    pub fn peek_mut(&mut self) -> Option<&mut T> {
        // Safety: current is a valid pointer if it's Some
        // and we have exclusive access to the list.
        // This upholds the safety invariant that all pointers in the list
        // point to valid memory.
        self.current.map(|node| unsafe { &mut (*node.as_ptr()).element })
    }

    /// Moves one position forward (towards the back) and
    /// returns a reference to the new position.
    ///
    /// Returns `None` if the cursor moves past the end of the list.
    #[allow(clippy::should_implement_trait)]
    pub fn next(&mut self) -> Option<&mut T> {
        if let Some(current) = self.current {
            // Safety: current is a valid pointer because of the list's invariants.
            // We're only dereferencing it to read the next pointer.
            let current_node = unsafe { &*current.as_ptr() };
            self.current = current_node.next;
            if self.current.is_some() {
                self.index += 1;
                self.peek_mut()
            } else {
                None
            }
        } else {
            None
        }
    }

    /// Moves one position backward (towards the front) and
    /// returns a reference to the new position.
    ///
    /// Returns `None` if the cursor moves past the beginning of the list.
    pub fn prev(&mut self) -> Option<&mut T> {
        if let Some(current) = self.current {
            // Safety: current is a valid pointer because of the list's invariants.
            // We're only dereferencing it to read the prev pointer.
            let current_node = unsafe { &*current.as_ptr() };
            self.current = current_node.prev;
            if self.current.is_some() {
                self.index -= 1;
                self.peek_mut()
            } else {
                None
            }
        } else {
            None
        }
    }

    /// Removes and returns the element at the current position and moves the cursor
    /// to the neighboring element that's closest to the back. This can be
    /// either the next or previous position.
    ///
    /// Returns `None` if the cursor is not pointing to any element.
    ///
    /// # Safety
    ///
    /// This method maintains the list's invariants by properly updating all pointers
    /// when removing a node. It handles all edge cases (removing from the head, tail,
    /// or middle of the list) and ensures no memory leaks or dangling pointers remain.
    pub fn take(&mut self) -> Option<T> {
        if let Some(current) = self.current {
            // Safety: current is a valid pointer because of the list's invariants.
            // We're taking ownership of the node to properly drop it.
            let node = unsafe { Box::from_raw(current.as_ptr()) };
            
            // Update the links
            let prev = node.prev;
            let next = node.next;
            
            match (prev, next) {
                (None, None) => {
                    // This was the only node
                    self.list.head = None;
                    self.list.tail = None;
                    self.current = None;
                }
                (Some(prev), None) => {
                    // This was the tail
                    // Safety: prev is a valid pointer
                    unsafe { (*prev.as_ptr()).next = None; }
                    self.list.tail = Some(prev);
                    self.current = Some(prev);
                    self.index -= 1;
                }
                (None, Some(next)) => {
                    // This was the head
                    // Safety: next is a valid pointer
                    unsafe { (*next.as_ptr()).prev = None; }
                    self.list.head = Some(next);
                    self.current = Some(next);
                    // index stays the same
                }
                (Some(prev), Some(next)) => {
                    // This was in the middle
                    // Safety: prev and next are valid pointers
                    unsafe {
                        (*prev.as_ptr()).next = Some(next);
                        (*next.as_ptr()).prev = Some(prev);
                    }
                    self.current = Some(next);
                    // index stays the same
                }
            }
            
            self.list.length -= 1;
            Some(node.element)
        } else {
            None
        }
    }

    /// Inserts a new element after the current position.
    ///
    /// If the list is empty or the cursor is at the end, the new element becomes
    /// the only element or the new tail, respectively.
    ///
    /// # Safety
    ///
    /// This method maintains the list's invariants by properly updating all pointers
    /// when inserting a new node. It handles all edge cases (inserting into an empty list,
    /// at the end, or in the middle) and ensures the linked structure remains valid.
    pub fn insert_after(&mut self, element: T) {
        match self.current {
            None => {
                // Empty list or at the end
                let new_node = Box::new(Node::new(element));
                let node_ptr = Node::into_ptr(new_node);
                
                if self.list.is_empty() {
                    // Empty list
                    self.list.head = Some(node_ptr);
                    self.list.tail = Some(node_ptr);
                    self.current = Some(node_ptr);
                } else {
                    // At the end
                    // Safety: tail is a valid pointer in a non-empty list
                    unsafe {
                        let tail = self.list.tail.unwrap();
                        (*node_ptr.as_ptr()).prev = Some(tail);
                        (*tail.as_ptr()).next = Some(node_ptr);
                    }
                    self.list.tail = Some(node_ptr);
                }
            }
            Some(current) => {
                // Insert after current
                let new_node = Box::new(Node::new(element));
                let node_ptr = Node::into_ptr(new_node);
                
                // Safety: current is a valid pointer
                unsafe {
                    let next = (*current.as_ptr()).next;
                    
                    // Link new node
                    (*node_ptr.as_ptr()).next = next;
                    (*node_ptr.as_ptr()).prev = Some(current);
                    
                    // Update current's next
                    (*current.as_ptr()).next = Some(node_ptr);
                    
                    // Update next's prev if it exists
                    if let Some(next) = next {
                        (*next.as_ptr()).prev = Some(node_ptr);
                    } else {
                        // This was the tail
                        self.list.tail = Some(node_ptr);
                    }
                }
            }
        }
        
        self.list.length += 1;
    }

    /// Inserts a new element before the current position.
    ///
    /// If the list is empty or the cursor is at the beginning, the new element becomes
    /// the only element or the new head, respectively.
    ///
    /// # Safety
    ///
    /// This method maintains the list's invariants by properly updating all pointers
    /// when inserting a new node. It handles all edge cases (inserting into an empty list,
    /// at the beginning, or in the middle) and ensures the linked structure remains valid.
    pub fn insert_before(&mut self, element: T) {
        match self.current {
            None => {
                // Empty list or at the beginning
                let new_node = Box::new(Node::new(element));
                let node_ptr = Node::into_ptr(new_node);
                
                if self.list.is_empty() {
                    // Empty list
                    self.list.head = Some(node_ptr);
                    self.list.tail = Some(node_ptr);
                    self.current = Some(node_ptr);
                } else {
                    // At the beginning
                    // Safety: head is a valid pointer in a non-empty list
                    unsafe {
                        let head = self.list.head.unwrap();
                        (*node_ptr.as_ptr()).next = Some(head);
                        (*head.as_ptr()).prev = Some(node_ptr);
                    }
                    self.list.head = Some(node_ptr);
                    self.index += 1;
                }
            }
            Some(current) => {
                // Insert before current
                let new_node = Box::new(Node::new(element));
                let node_ptr = Node::into_ptr(new_node);
                
                // Safety: current is a valid pointer
                unsafe {
                    let prev = (*current.as_ptr()).prev;
                    
                    // Link new node
                    (*node_ptr.as_ptr()).prev = prev;
                    (*node_ptr.as_ptr()).next = Some(current);
                    
                    // Update current's prev
                    (*current.as_ptr()).prev = Some(node_ptr);
                    
                    // Update prev's next if it exists
                    if let Some(prev) = prev {
                        (*prev.as_ptr()).next = Some(node_ptr);
                    } else {
                        // This was the head
                        self.list.head = Some(node_ptr);
                    }
                }
                
                self.index += 1;
            }
        }
        
        self.list.length += 1;
    }
}

/// Implementation of the Iterator trait for Iter.
///
/// This allows iterating over the elements of the linked list from front to back.
impl<'a, T> Iterator for Iter<'a, T> {
    type Item = &'a T;

    /// Returns the next element in the iteration.
    ///
    /// # Safety
    ///
    /// This method is safe because it only accesses nodes that are guaranteed
    /// to be valid by the list's invariants. The lifetime of the returned reference
    /// is tied to the lifetime of the iterator, which is tied to the lifetime of the list.
    fn next(&mut self) -> Option<&'a T> {
        if self.remaining == 0 {
            return None;
        }
        
        self.current.map(|node| {
            // Safety: node is a valid pointer and we're returning a reference
            // with the correct lifetime. The node is guaranteed to exist for the
            // lifetime of the iterator.
            let node_ref = unsafe { &*node.as_ptr() };
            self.current = node_ref.next;
            self.remaining -= 1;
            &node_ref.element
        })
    }
}

/// Implementation of the Drop trait to clean up resources.
///
/// This ensures that all nodes in the list are properly deallocated when the list
/// is dropped, preventing memory leaks.
impl<T> Drop for LinkedList<T> {
    /// Drops all nodes in the list.
    ///
    /// # Safety
    ///
    /// This method is safe because it carefully manages the deallocation of each node,
    /// ensuring that:
    /// 1. Each node is deallocated exactly once
    /// 2. No dangling pointers remain
    /// 3. No double-free occurs
    fn drop(&mut self) {
        // Take ownership of the head and manually drop each node
        while let Some(node) = self.head {
            // Safety: node is a valid pointer
            unsafe {
                // Convert the raw pointer back to a Box and drop it
                let mut node_box = Box::from_raw(node.as_ptr());
                self.head = node_box.next;
                
                // Manually set next and prev to None to avoid double-free
                node_box.next = None;
                node_box.prev = None;
                
                // Box will be dropped at the end of this scope
            }
        }
        
        // Reset the list state
        self.tail = None;
        self.length = 0;
    }
}

/// Implement Send for LinkedList<T> if T is Send.
///
/// # Safety
///
/// This is safe because we maintain the invariants of the linked list
/// and properly manage the raw pointers. The list never allows multiple threads
/// to concurrently access the same node, and all access to nodes is properly
/// synchronized through the list's API.
unsafe impl<T: Send> Send for LinkedList<T> {}

/// Implement Sync for LinkedList<T> if T is Sync.
///
/// # Safety
///
/// This is safe because we maintain the invariants of the linked list
/// and properly manage the raw pointers. The list ensures that all shared
/// references to nodes are valid and that no mutable references can be
/// created while shared references exist.
unsafe impl<T: Sync> Sync for LinkedList<T> {}
