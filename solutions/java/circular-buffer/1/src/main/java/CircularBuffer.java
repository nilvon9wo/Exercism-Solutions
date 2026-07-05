class CircularBuffer<T> {
    private final CircularBufferState<T> circularBufferState;

    CircularBuffer(final int size) {
        this.circularBufferState = new CircularBufferState<T>(size);
    }

    T read() throws BufferIOException {
        if (this.circularBufferState.isEmpty()) {
            throw new BufferIOException("Tried to read from empty buffer");
        }

        T value = this.circularBufferState.readCurrentElement();
        this.circularBufferState.removeCurrentElement();
        return value;
    }

    void write(final T element) throws BufferIOException {
        if (this.circularBufferState.isFull()) {
            throw new BufferIOException("Tried to write to full buffer");
        }

        this.circularBufferState.append(element);
    }

    void overwrite(final T element) {
        if (this.circularBufferState.isFull()) {
            this.circularBufferState.advanceIndex()
                                    .decreaseElementCount();
        }

        this.circularBufferState.append(element);
    }

    void clear() {
        while (!this.circularBufferState.isEmpty()) {
            this.circularBufferState.removeCurrentElement();
        }

        this.circularBufferState.resetIndexes();
    }
}