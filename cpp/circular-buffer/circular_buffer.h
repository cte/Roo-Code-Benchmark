#if !defined(CIRCULAR_BUFFER_H)
#define CIRCULAR_BUFFER_H

#include <vector>
#include <stdexcept>

namespace circular_buffer {

template <typename T>
class circular_buffer {
private:
    std::vector<T> buffer;
    size_t capacity;
    size_t size;
    size_t read_pos;
    size_t write_pos;

public:
    circular_buffer(size_t capacity)
        : buffer(capacity)
        , capacity(capacity)
        , size(0)
        , read_pos(0)
        , write_pos(0)
    {}

    T read() {
        if (size == 0) {
            throw std::domain_error("Buffer is empty");
        }

        T value = buffer[read_pos];
        read_pos = (read_pos + 1) % capacity;
        size--;
        return value;
    }

    void write(T item) {
        if (size == capacity) {
            throw std::domain_error("Buffer is full");
        }

        buffer[write_pos] = item;
        write_pos = (write_pos + 1) % capacity;
        size++;
    }

    void clear() {
        size = 0;
        read_pos = 0;
        write_pos = 0;
    }

    void overwrite(T item) {
        if (size < capacity) {
            write(item);
        } else {
            // Buffer is full, overwrite the oldest item
            buffer[read_pos] = item;
            read_pos = (read_pos + 1) % capacity;
            // write_pos doesn't change because we're replacing an existing item
        }
    }
};

}  // namespace circular_buffer

#endif // CIRCULAR_BUFFER_H