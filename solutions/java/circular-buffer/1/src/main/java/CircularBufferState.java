public class CircularBufferState<T> {
    CircularBufferState(final int size) {
        this.elements = new Object[size];
    }

    private final Object[] elements;
    T readCurrentElement() {
        //noinspection unchecked
        return (T) this.elements[this.readIndex];
    }

    @SuppressWarnings("UnusedReturnValue")
    CircularBufferState removeCurrentElement() {
        this.elements[this.readIndex] = null;
        this.advanceIndex();
        this.decreaseElementCount();
        return this;
    }

    private int readIndex;
    CircularBufferState advanceIndex() {
        this.readIndex = advance(this.readIndex);
        return this;
    }

    private int writeIndex;
    private CircularBufferState advanceWriteIndex() {
        this.writeIndex = advance(this.writeIndex);
        return this;
    }

    private int advance(final int index) {
        return (index + 1) % this.elements.length;
    }

    @SuppressWarnings("UnusedReturnValue")
    CircularBufferState append(final T element) {
        this.storeCurrentElement(element)
                .advanceWriteIndex()
                .increaseElementCount();
        return this;
    }

    private CircularBufferState storeCurrentElement(final T element) {
        this.elements[this.writeIndex] = element;
        return this;
    }

    private int elementCount;
    @SuppressWarnings("UnusedReturnValue")
    private CircularBufferState increaseElementCount() {
        this.elementCount++;
        return this;
    }
    @SuppressWarnings("UnusedReturnValue")
    CircularBufferState decreaseElementCount() {
        this.elementCount--;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    CircularBufferState resetIndexes() {
        this.readIndex = 0;
        this.writeIndex = 0;
        return this;
    }

    boolean isEmpty() {
        return this.elementCount == 0;
    }

    boolean isFull() {
        return this.elementCount == this.elements.length;
    }
}
