object VariableLengthQuantity {
    private val SevenBitMask: Int = 0x7F
    private val ContinuationBitMask: Int = 0x80
    private val BitsPerChunk: Int = 7
    private val BitsPerByte: Int = 8
    private val EmptyValue: Int = 0

    def encode(values: List[Int]): List[Int] =
        values.flatMap(encodeSingleValue)

    private def encodeSingleValue(value: Int): List[Int] =
        val unsignedValue: Long = value.toLong & 0xFFFFFFFFL
        val base128Chunks: List[Int] = splitIntoBase128Chunks(unsignedValue)
        addContinuationMarkers(base128Chunks)

    private def splitIntoBase128Chunks(value: Long): List[Int] =
        var remainingValue = value
        var reversedChunks = List.empty[Int]
        remainingValue match
            case EmptyValue => List(EmptyValue)
            case _ =>
                while remainingValue > 0 do
                    val chunk = (remainingValue & SevenBitMask.toLong).toInt
                    reversedChunks = chunk :: reversedChunks
                    remainingValue = remainingValue >>> BitsPerChunk
                reversedChunks

    private def addContinuationMarkers(chunks: List[Int]): List[Int] =
        chunks match
            case Nil =>
                Nil
            case lastChunk :: Nil =>
                List(lastChunk)
            case firstChunk :: remainingChunks =>
                (firstChunk | ContinuationBitMask) :: addContinuationMarkers(remainingChunks)

    def decode(encodedBytes: List[Int]): Either[String, List[Int]] =
        val decodedResult = decodeLoop(encodedBytes, 0L, Nil)
        if isInvalidIncompleteSequence(encodedBytes, decodedResult)
        then Left("Incomplete sequence")
        else decodedResult

    private def decodeLoop(
                      remainingBytes: List[Int],
                      currentValue: Long,
                      decodedValues: List[Long]
                  ): Either[String, List[Int]] =
        remainingBytes match
            case Nil =>
                Right(decodedValues.map(_.toInt))
            case currentByte :: restOfBytes =>
                processByte(currentByte, decodedValues, currentValue, restOfBytes)

    private def processByte(
                               byte: Int,
                               decodedValues: List[Long],
                               currentValue: Long,
                               restOfBytes: List[Int]
                           ) =
        val hasContinuationBitSet: Boolean = (byte & ContinuationBitMask) != 0
        val sevenBitValue: Long = (byte & SevenBitMask).toLong
        val updatedValue: Long = (currentValue << BitsPerChunk) | sevenBitValue

        if hasContinuationBitSet
        then decodeLoop(restOfBytes, updatedValue, decodedValues)
        else {
            decodeLoop(restOfBytes, 0L, decodedValues :+ updatedValue)
        }

    private def isInvalidIncompleteSequence(
                                               inputBytes: List[Int],
                                               decodedResult: Either[String, List[Int]]
                                           ): Boolean =
        inputBytes.nonEmpty
            && (inputBytes.last & SevenBitMask) != inputBytes.last
            && decodedResult.isRight
}