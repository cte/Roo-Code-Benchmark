#if !defined(BINARY_SEARCH_TREE_H)
#define BINARY_SEARCH_TREE_H

#include <memory>
#include <stack>

namespace binary_search_tree {

template <typename T>
class binary_tree {
public:
    // Constructor
    binary_tree(const T& data) : data_(data), left_(nullptr), right_(nullptr) {}

    // Data accessor
    T data() const { return data_; }

    // Tree accessors
    std::unique_ptr<binary_tree<T>>& left() { return left_; }
    const std::unique_ptr<binary_tree<T>>& left() const { return left_; }
    
    std::unique_ptr<binary_tree<T>>& right() { return right_; }
    const std::unique_ptr<binary_tree<T>>& right() const { return right_; }

    // Insert a new value into the tree
    void insert(const T& value) {
        if (value <= data_) {
            // Insert to the left
            if (left_) {
                left_->insert(value);
            } else {
                left_ = std::make_unique<binary_tree<T>>(value);
            }
        } else {
            // Insert to the right
            if (right_) {
                right_->insert(value);
            } else {
                right_ = std::make_unique<binary_tree<T>>(value);
            }
        }
    }

    // Iterator implementation for in-order traversal
    class iterator {
    public:
        using iterator_category = std::forward_iterator_tag;
        using value_type = T;
        using difference_type = std::ptrdiff_t;
        using pointer = T*;
        using reference = T&;

        iterator() : current_(nullptr) {}
        
        explicit iterator(binary_tree<T>* root) {
            if (root) {
                current_ = root;
                // Go to the leftmost node
                while (current_->left_) {
                    node_stack_.push(current_);
                    current_ = current_->left_.get();
                }
            }
        }

        // Return a reference to the data instead of a copy
        T& operator*() const { return current_->data_; }
        T* operator->() const { return &(current_->data_); }

        iterator& operator++() {
            // If we have a right child, go to it and then all the way left
            if (current_->right_) {
                current_ = current_->right_.get();
                while (current_->left_) {
                    node_stack_.push(current_);
                    current_ = current_->left_.get();
                }
            } else if (!node_stack_.empty()) {
                // Otherwise, go back up to the parent
                current_ = node_stack_.top();
                node_stack_.pop();
            } else {
                // No more nodes to visit
                current_ = nullptr;
            }
            return *this;
        }

        iterator operator++(int) {
            iterator tmp = *this;
            ++(*this);
            return tmp;
        }

        bool operator==(const iterator& other) const {
            return current_ == other.current_;
        }

        bool operator!=(const iterator& other) const {
            return !(*this == other);
        }

    private:
        binary_tree<T>* current_;
        std::stack<binary_tree<T>*> node_stack_;
    };

    // Begin and end iterators
    iterator begin() { return iterator(this); }
    iterator end() { return iterator(); }

private:
    T data_;
    std::unique_ptr<binary_tree<T>> left_;
    std::unique_ptr<binary_tree<T>> right_;
};

}  // namespace binary_search_tree

#endif // BINARY_SEARCH_TREE_H