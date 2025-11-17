class AnnalynsInfiltration {
    public static boolean canFastAttack(boolean isKnightAwake) {
        return !isKnightAwake;
    }

    public static boolean canSpy(boolean isKnightAwake, boolean isArcherAwake, boolean isPrisonerAwake) {
        return isKnightAwake
        || isArcherAwake
        || isPrisonerAwake;
    }

    public static boolean canSignalPrisoner(boolean isArcherAwake, boolean isPrisonerAwake) {
        return !isArcherAwake
               && isPrisonerAwake;
    }

    public static boolean canFreePrisoner(boolean isKnightAwake, boolean isArcherAwake, boolean isPrisonerAwake, boolean isPetDogPresent) {
        return !isArcherAwake
               && (
                       isPetDogPresent
                       || (isPrisonerAwake && !isKnightAwake)
               );
    }
}
