DIVISOR_FOR_PLING = 3
DIVISOR_FOR_PLANG = 5
DIVISOR_FOR_PLONG = 7

SOUND_FOR_PLING = "Pling"
SOUND_FOR_PLANG = "Plang"
SOUND_FOR_PLONG = "Plong"

def convert(number):
    result = ""
    if number % DIVISOR_FOR_PLING == 0:
        result += SOUND_FOR_PLING

    if number % DIVISOR_FOR_PLANG == 0:
        result += SOUND_FOR_PLANG

    if number % DIVISOR_FOR_PLONG == 0:
        result += SOUND_FOR_PLONG

    if not result:
        return str(number)

    return result