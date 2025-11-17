using System;
using System.Linq;

public static class TelemetryBuffer
{
	private const byte SendLength = 9;
	private const byte LongPrefix = 0xf8;
	private const byte UIntPrefix = 0x4;
	private const byte IntPrefix = 0xfc;
	private const byte UShortPrefix = 0x2;
	private const byte ShortPrefix = 0xfe;

	public static byte[] ToBuffer(long reading)
	{
		(byte prefix, byte[] bytesForType) = reading switch
		{
			>= 4_294_967_296 => (LongPrefix, BitConverter.GetBytes(reading)),
			>= 2_147_483_648 => (UIntPrefix, BitConverter.GetBytes((uint)reading)),
			>= 65_536 => (IntPrefix, BitConverter.GetBytes((int)reading)),
			>= 0 => (UShortPrefix, BitConverter.GetBytes((ushort)reading)),
			>= -32_768 => (ShortPrefix, BitConverter.GetBytes((short)reading)),
			>= -2_147_483_648 => (IntPrefix, BitConverter.GetBytes((int)reading)),
			_ => (LongPrefix, BitConverter.GetBytes(reading)),
		};

		byte[] bytesForSend = PrependPrefix(prefix, bytesForType);
		return ByteArrayRightPad(bytesForSend);
	}

	private static byte[] ByteArrayRightPad(byte[] input)
	{
		byte[] temp = Enumerable.Repeat((byte)0, SendLength)
			.ToArray();

		for (int i = 0; i < input.Length; i++)
		{
			temp[i] = input[i];
		}

		return temp;
	}

	private static byte[] PrependPrefix(byte prefix, byte[] bytes)
	{
		byte[] newValues = new byte[bytes.Length + 1];
		newValues[0] = prefix;
		Array.Copy(bytes, 0, newValues, 1, bytes.Length);
		return newValues;
	}

	public static long FromBuffer(byte[] buffer)
	{
		byte prefix = buffer[0];
		buffer = buffer.Skip(1)
			.ToArray();

		return prefix switch
		{
			LongPrefix => BitConverter.ToInt64(buffer),
			UIntPrefix => BitConverter.ToUInt32(buffer),
			IntPrefix => BitConverter.ToInt32(buffer),
			UShortPrefix => BitConverter.ToUInt16(buffer),
			ShortPrefix => BitConverter.ToInt16(buffer),
			_ => 0,
		};
	}
}
