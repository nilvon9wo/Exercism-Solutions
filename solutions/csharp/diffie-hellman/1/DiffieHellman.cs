using System.Numerics;
using System.Security.Cryptography;

// ReSharper disable once CheckNamespace
public static class DiffieHellman
{
	public static BigInteger PrivateKey(BigInteger primeP)
	{
		BigInteger number;
		using RandomNumberGenerator random = RandomNumberGenerator.Create();
		do
		{
			number = GenerateNumber(random, primeP);
		} while (IsOutOfRange(primeP, number));

		return number;
	}

	private static bool IsOutOfRange(BigInteger primeP, BigInteger number)
		=> (number <= 1)
		   || (number >= primeP);

	private static BigInteger GenerateNumber(RandomNumberGenerator randomGenerator, BigInteger maxExclusive)
	{
		int byteLength = maxExclusive.ToByteArray()
			.Length;
		byte[] randomBytes = new byte[byteLength];
		randomGenerator.GetBytes(randomBytes);
		BigInteger randomBigInteger = new(randomBytes);
		return BigInteger.Abs(randomBigInteger);
	}

	public static BigInteger PublicKey(BigInteger primeP, BigInteger primeG, BigInteger privateKey)
		=> BigInteger.ModPow(primeG, privateKey, primeP);

	public static BigInteger Secret(BigInteger primeP, BigInteger publicKey, BigInteger privateKey)
		=> BigInteger.ModPow(publicKey, privateKey, primeP);
}