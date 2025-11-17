internal class Lasagna
{
	private const int _expectedCookingTime = 40;
	private const int _preparationMinutesPerLayer = 2;

	internal int ElapsedTimeInMinutes(int layerCount, int elapsedTime) =>
		elapsedTime + PreparationTimeInMinutes(layerCount);

	internal int ExpectedMinutesInOven() =>
		_expectedCookingTime;

	internal int PreparationTimeInMinutes(int layerCount) =>
		layerCount * _preparationMinutesPerLayer;

	internal int RemainingMinutesInOven(int elapsedTime) =>
		_expectedCookingTime - elapsedTime;
}
