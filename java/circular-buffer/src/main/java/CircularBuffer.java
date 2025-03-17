class CircularBuffer<T> {
    private final T[] buffer;
    private int readIndex;
    private int writeIndex;
    private int size;
    private int count;

    @SuppressWarnings("unchecked")
    CircularBuffer(final int size) {
        this.buffer = (T[]) new Object[size];
        this.readIndex = 0;
        this.writeIndex = 0;
        this.size = size;
        this.count = 0;
    }

    T read() throws BufferIOException {
        if (count == 0) {
            throw new BufferIOException("Tried to read from empty buffer");
        }
        
        T data = buffer[readIndex];
        buffer[readIndex] = null; // Clear the reference to help with garbage collection
        readIndex = (readIndex + 1) % size;
        count--;
        
        return data;
    }

    void write(T data) throws BufferIOException {
        if (count == size) {
            throw new BufferIOException("Tried to write to full buffer");
        }
        
        buffer[writeIndex] = data;
        writeIndex = (writeIndex + 1) % size;
        count++;
    }

    void overwrite(T data) {
        if (count < size) {
            try {
                write(data);
            } catch (BufferIOException e) {
                // This should never happen since we check count < size
            }
        } else {
            // Buffer is full, overwrite the oldest element
            buffer[readIndex] = data;
            readIndex = (readIndex + 1) % size;
            writeIndex = (writeIndex + 1) % size;
            // Count remains the same as we're replacing an element
        }
    }

    void clear() {
        for (int i = 0; i < size; i++) {
            buffer[i] = null;
        }
        readIndex = 0;
        writeIndex = 0;
        count = 0;
    }
}