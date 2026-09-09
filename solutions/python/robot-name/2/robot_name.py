import random
import string
from typing import ClassVar

NAME_LETTER_COUNT = 2
NAME_DIGIT_COUNT = 3
DIGIT_MINIMUM = 0
DIGIT_MAXIMUM = 999

# pylint: disable=too-few-public-methods
class Robot:
    _used_names: ClassVar[set[str]] = set()

    def __init__(self):
        self._name = None

    @property
    def name(self):
        if self._name is None:
            self._name = self._generate_unique_name()

        return self._name

    def reset(self):
        self._name = None

    def _generate_unique_name(self):
        while True:
            name = self._generate_name()
            if name not in Robot._used_names:
                Robot._used_names.add(name)
                return name

    @staticmethod
    def _generate_name():
        letters = ""
        # pylint: disable=disallowed-name
        for _ in range(NAME_LETTER_COUNT):
            letters += random.choice(string.ascii_uppercase)

        number = random.randint(DIGIT_MINIMUM, DIGIT_MAXIMUM)
        digits = f"{number:0{NAME_DIGIT_COUNT}d}"
        return letters + digits