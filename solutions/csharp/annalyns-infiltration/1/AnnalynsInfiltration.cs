internal static class QuestLogic
{
	public static bool CanFastAttack(bool isKnightAwake) =>
		!isKnightAwake;

	public static bool CanSpy(bool isKnightAwake, bool isArcherAwake, bool isPrisonerAwake) =>
		isKnightAwake
		|| isArcherAwake
		|| isPrisonerAwake;

	public static bool CanSignalPrisoner(bool isArcherAwake, bool isPrisonerAwake) =>
		!isArcherAwake
		&& isPrisonerAwake;

	public static bool CanFreePrisoner(bool isKnightAwake, bool isArcherAwake, bool isPrisonerAwake, bool isPetDogPresent) =>
		!isArcherAwake
		&& (
			isPetDogPresent
			|| (!isPetDogPresent && isPrisonerAwake && !isKnightAwake)
		);
}
