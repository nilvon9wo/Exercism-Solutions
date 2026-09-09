"""Provide functionality for validating and formatting North American phone numbers."""

import re

COUNTRY_CODE = "1"
EXPECTED_DIGIT_COUNT = 10
MAXIMUM_DIGIT_COUNT = 11
AREA_CODE_LENGTH = 3
EXCHANGE_CODE_START = 3
EXCHANGE_CODE_END = 6
SUBSCRIBER_START = 6

VALID_PUNCTUATION = " -().+"

NUMBER_PATTERN = re.compile(r"^[0-9]+$")


class PhoneNumber:  # pylint: disable=too-few-public-methods
    """Represent a validated North American phone number."""

    def __init__(self, number: str):
        self.number: str = self._clean(number)

    @property
    def area_code(self) -> str:
        return self.number[:AREA_CODE_LENGTH]

    def pretty(self) -> str:
        area_code = self.number[:AREA_CODE_LENGTH]
        exchange_code = self.number[EXCHANGE_CODE_START:EXCHANGE_CODE_END]
        subscriber_number = self.number[SUBSCRIBER_START:]
        return f"({area_code})-{exchange_code}-{subscriber_number}"

    @staticmethod
    def _clean(number: str) -> str:
        if PhoneNumber._contains_letters(number):
            raise ValueError("letters not permitted")

        if PhoneNumber._contains_invalid_characters(number):
            raise ValueError("punctuations not permitted")

        digits = re.sub(r"\D", "", number)

        if len(digits) < EXPECTED_DIGIT_COUNT:
            raise ValueError("must not be fewer than 10 digits")

        if len(digits) > MAXIMUM_DIGIT_COUNT:
            raise ValueError("must not be greater than 11 digits")

        if len(digits) == MAXIMUM_DIGIT_COUNT:
            if not digits.startswith(COUNTRY_CODE):
                raise ValueError("11 digits must start with 1")

            digits = digits[1:]

        if digits[0] == "0":
            raise ValueError("area code cannot start with zero")

        if digits[0] == "1":
            raise ValueError("area code cannot start with one")

        if digits[EXCHANGE_CODE_START] == "0":
            raise ValueError("exchange code cannot start with zero")

        if digits[EXCHANGE_CODE_START] == "1":
            raise ValueError("exchange code cannot start with one")

        return digits

    @staticmethod
    def _contains_letters(number: str) -> bool:
        return any(
            character.isalpha()
            for character in number
        )

    @staticmethod
    def _contains_invalid_characters(number: str) -> bool:
        return any(
            not character.isdigit() and character not in VALID_PUNCTUATION
            for character in number
        )
