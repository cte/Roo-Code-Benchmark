package paasio

import (
	"io"
	"sync"
)

// readCounter implements ReadCounter
type readCounter struct {
	reader    io.Reader
	bytesRead int64
	readOps   int
	mu        sync.Mutex
}

// writeCounter implements WriteCounter
type writeCounter struct {
	writer       io.Writer
	bytesWritten int64
	writeOps     int
	mu           sync.Mutex
}

// readWriteCounter implements ReadWriteCounter
type readWriteCounter struct {
	*readCounter
	*writeCounter
}

func NewWriteCounter(writer io.Writer) WriteCounter {
	return &writeCounter{
		writer:       writer,
		bytesWritten: 0,
		writeOps:     0,
	}
}

func NewReadCounter(reader io.Reader) ReadCounter {
	return &readCounter{
		reader:    reader,
		bytesRead: 0,
		readOps:   0,
	}
}

func NewReadWriteCounter(readwriter io.ReadWriter) ReadWriteCounter {
	return &readWriteCounter{
		readCounter:  NewReadCounter(readwriter).(*readCounter),
		writeCounter: NewWriteCounter(readwriter).(*writeCounter),
	}
}

func (rc *readCounter) Read(p []byte) (int, error) {
	n, err := rc.reader.Read(p)
	
	rc.mu.Lock()
	rc.bytesRead += int64(n)
	rc.readOps++
	rc.mu.Unlock()
	
	return n, err
}

func (rc *readCounter) ReadCount() (int64, int) {
	rc.mu.Lock()
	defer rc.mu.Unlock()
	return rc.bytesRead, rc.readOps
}

func (wc *writeCounter) Write(p []byte) (int, error) {
	n, err := wc.writer.Write(p)
	
	wc.mu.Lock()
	wc.bytesWritten += int64(n)
	wc.writeOps++
	wc.mu.Unlock()
	
	return n, err
}

func (wc *writeCounter) WriteCount() (int64, int) {
	wc.mu.Lock()
	defer wc.mu.Unlock()
	return wc.bytesWritten, wc.writeOps
}
