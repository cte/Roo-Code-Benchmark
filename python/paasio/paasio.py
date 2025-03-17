import io
import errno
import os


class MeteredFile(io.BufferedRandom):
    """Implement using a subclassing model."""

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._read_bytes_count = 0
        self._read_ops_count = 0
        self._write_bytes_count = 0
        self._write_ops_count = 0

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        return super().__exit__(exc_type, exc_val, exc_tb)

    def __iter__(self):
        return self

    def __next__(self):
        try:
            line = self.readline()
            if not line:
                raise StopIteration
            self._read_bytes_count += len(line)
            self._read_ops_count += 1
            return line
        except ValueError:
            # If the file is not initialized, delegate to the mock object
            mock = super().readline()
            if not mock:
                raise StopIteration
            self._read_bytes_count += len(mock)
            self._read_ops_count += 1
            return mock

    def read(self, size=-1):
        data = super().read(size)
        self._read_bytes_count += len(data) if data else 0
        self._read_ops_count += 1
        return data

    @property
    def read_bytes(self):
        return self._read_bytes_count

    @property
    def read_ops(self):
        return self._read_ops_count

    def write(self, b):
        bytes_written = super().write(b)
        self._write_bytes_count += bytes_written
        self._write_ops_count += 1
        return bytes_written

    @property
    def write_bytes(self):
        return self._write_bytes_count

    @property
    def write_ops(self):
        return self._write_ops_count


class MeteredSocket:
    """Implement using a delegation model."""

    def __init__(self, socket):
        self._socket = socket
        self._recv_bytes_count = 0
        self._recv_ops_count = 0
        self._send_bytes_count = 0
        self._send_ops_count = 0
        self._closed = False

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self._closed = True
        return self._socket.__exit__(exc_type, exc_val, exc_tb)

    def recv(self, bufsize, flags=0):
        if self._closed:
            raise OSError(errno.EBADF, os.strerror(errno.EBADF))
        data = self._socket.recv(bufsize, flags)
        self._recv_bytes_count += len(data) if data else 0
        self._recv_ops_count += 1
        return data

    @property
    def recv_bytes(self):
        return self._recv_bytes_count

    @property
    def recv_ops(self):
        return self._recv_ops_count

    def send(self, data, flags=0):
        if self._closed:
            raise OSError(errno.EBADF, os.strerror(errno.EBADF))
        bytes_sent = self._socket.send(data, flags)
        self._send_bytes_count += bytes_sent
        self._send_ops_count += 1
        return bytes_sent

    @property
    def send_bytes(self):
        return self._send_bytes_count

    @property
    def send_ops(self):
        return self._send_ops_count
