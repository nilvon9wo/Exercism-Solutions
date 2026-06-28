class EmptyBufferException
    extends Exception {}

class FullBufferException
    extends Exception {}

class CircularBuffer(var capacity: Int):
    private val data: Array[Int] = new Array[Int](capacity)
    private var readIndex: Int = 0
    private var writeIndex: Int = 0
    private var currentSize: Int = 0

    def write(value: Int): Unit = {
        if (currentSize == capacity)
            throw new FullBufferException()

        data(writeIndex) = value
        writeIndex = (writeIndex + 1) % capacity
        currentSize += 1
    }

    def read(): Int =
        if (currentSize == 0)
            throw new EmptyBufferException()

        val value = data(readIndex)
        readIndex = (readIndex + 1) % capacity
        currentSize -= 1
        value

    def overwrite(value: Int): Unit =
        if currentSize == capacity
        then writeAndAdvance(value)
        else write(value)

    private def writeAndAdvance(value: Int): Unit =
        data(writeIndex) = value
        writeIndex = (writeIndex + 1) % capacity
        readIndex = writeIndex

    def clear(): Unit =
        readIndex = 0
        writeIndex = 0
        currentSize = 0
