import random

NUMBER_OF_DICE = 4
DICE_SIDES = 6
DICE_TO_KEEP = 3
ABILITY_COUNT = 6
BASE_HITPOINTS = 10

def modifier(score):
    return (score - 10) // 2

class Character:  # pylint: disable=too-few-public-methods
    def __init__(self):
        self.strength = self.ability()
        self.dexterity = self.ability()
        self.constitution = self.ability()
        self.intelligence = self.ability()
        self.wisdom = self.ability()
        self.charisma = self.ability()
        self.hitpoints = BASE_HITPOINTS + modifier(self.constitution)

    # noinspection method-may-be-static
    # noinspection no-self-use
    def ability(self):
        rolls = []

        # pylint: disable=disallowed-name
        for _ in range(NUMBER_OF_DICE):
            rolls.append(random.randint(1, DICE_SIDES))

        rolls.sort()
        return sum(rolls[-DICE_TO_KEEP:])