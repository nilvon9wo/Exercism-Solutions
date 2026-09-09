class Luhn:
    def __init__(self, card_number):
        self.card_number = card_number

    def valid(self):
        normalized_number = self.card_number.replace(" ", "")
        if len(normalized_number) <= 1:
            return False

        if not _contains_only_digits(normalized_number):
            return False

        checksum = _calculate_checksum(normalized_number)
        return checksum % 10 == 0

def _contains_only_digits(number):
    return all(
            character.isdigit()
            for character in number
    )

def _calculate_checksum(number):
    checksum = 0
    should_double = len(number) % 2 == 0
    for character in number:
        digit = int(character)

        if should_double:
            digit *= 2
            if digit > 9:
                digit -= 9

        checksum += digit
        should_double = not should_double

    return checksum