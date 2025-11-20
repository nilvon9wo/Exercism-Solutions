#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050
public static class IntergalacticTransmission
{
    private const int BitsPerByte = 8;
    private const int DataBitsPerTransmission = 7;
    private const int ParityBitMask = 1;
    private const int EvenParityRemainder = 0;
    private const int OddParityRemainder = 1;
    private const int MostSignificantBitPosition = 7;
    private const int LeastSignificantBitPosition = 0;
    private const int DataBitStartPosition = 1;

    public static byte[] GetTransmitSequence(byte[] originalMessage)
        => originalMessage.Length == 0
            ? []
            : [.. ConvertMessageToBitStream(originalMessage)
                .Chunk(DataBitsPerTransmission)
                .Select(CreateTransmissionByteWithParity)
            ];

    public static byte[] DecodeSequence(byte[] receivedTransmissionSequence)
    {
        if (receivedTransmissionSequence.Length == 0)
        {
            return [];
        }

        bool[] allDataBits = ExtractAllDataBitsFromTransmissions(receivedTransmissionSequence);
        return ConvertBitStreamToBytes(allDataBits);
    }

    private static bool[] ExtractAllDataBitsFromTransmissions(byte[] receivedTransmissionSequence)
        => [.. 
            receivedTransmissionSequence.SelectMany(receivedTransmissionByte =>
            {
                ValidateParityBit(receivedTransmissionByte);
                return ExtractDataBitsFromTransmission(receivedTransmissionByte);
            })
        ];

    private static bool[] ConvertMessageToBitStream(byte[] originalMessage)
        => [.. originalMessage.SelectMany(ExtractBitsFromByte) ];

    private static bool[] ExtractBitsFromByte(byte messageByte)
        => [..
            Enumerable.Range(LeastSignificantBitPosition, BitsPerByte)
                .Select(bitIndex => IsBitSetAtPosition(messageByte, bitIndex))
        ];

    private static bool IsBitSetAtPosition(byte messageByte, int bitIndex)
    {
        int bitPosition = CalculateBitPosition(bitIndex);
        int bitMask = ParityBitMask << bitPosition;
        return (messageByte & bitMask) != 0;
    }

    private static byte CreateTransmissionByteWithParity(bool[] originalDataBits)
    {
        bool[] paddedDataBits = PadDataBitsToFixedLength(originalDataBits);
        int onesCountInDataBits = paddedDataBits.Count(dataBit => dataBit);
        bool parityBitValue = (onesCountInDataBits % 2) == OddParityRemainder;
        byte transmissionByteWithoutParity = BuildByteFromDataBits(paddedDataBits);
        return parityBitValue
            ? (byte)(transmissionByteWithoutParity | ParityBitMask)
            : transmissionByteWithoutParity;
    }

    private static byte BuildByteFromDataBits(bool[] paddedDataBits)
        => (byte)paddedDataBits
            .Select(ConvertBitToByteValue)
            .Sum();

    private static int ConvertBitToByteValue(bool dataBit, int bitIndex)
        => dataBit
            ? (ParityBitMask << CalculateBitPosition(bitIndex))
            : 0;

    private static int CalculateBitPosition(int bitIndex)
        => MostSignificantBitPosition - bitIndex;

    private static bool[] PadDataBitsToFixedLength(bool[] originalDataBits)
        => [..
            originalDataBits.Concat(Enumerable.Repeat(false, DataBitsPerTransmission))
                .Take(DataBitsPerTransmission)
        ];

    private static void ValidateParityBit(byte receivedTransmissionByte)
    {
        int totalOnesCount = System.Numerics.BitOperations.PopCount(receivedTransmissionByte);
        if (totalOnesCount % 2 != EvenParityRemainder)
        {
            throw new ArgumentException("Invalid parity in received sequence.");
        }
    }

    private static bool[] ExtractDataBitsFromTransmission(byte receivedTransmissionByte)
        => [..
            Enumerable.Range(DataBitStartPosition, DataBitsPerTransmission)
                .Select(bitPosition => IsDataBitSet(receivedTransmissionByte, bitPosition))
        ];

    private static bool IsDataBitSet(byte receivedTransmissionByte, int bitPosition)
    {
        int bitMask = ParityBitMask << (BitsPerByte - bitPosition);
        return (receivedTransmissionByte & bitMask) != 0;
    }

    private static byte[] ConvertBitStreamToBytes(bool[] reconstructedDataBits)
    {
        byte[] bytes = reconstructedDataBits
            .Chunk(BitsPerByte)
            .Select(ConvertBitChunkToByte)
            .ToArray();

        return RemoveTrailingZeroBytes(bytes);
    }

    private static byte[] RemoveTrailingZeroBytes(byte[] bytes)
    {
        int lastNonZeroIndex = Array.FindLastIndex(bytes, b => b != 0);
        return lastNonZeroIndex == -1 
            ? bytes.Length > 0 
                ? [0] 
                : [] 
            : bytes[..(lastNonZeroIndex + 1)];
    }

    private static byte ConvertBitChunkToByte(bool[] byteBits)
        => (byte)byteBits
            .Select(ConvertBitToByteValue)
            .Sum();
}