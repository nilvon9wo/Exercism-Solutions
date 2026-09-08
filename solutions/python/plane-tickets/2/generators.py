SEAT_LETTERS = ("A", "B", "C", "D")
SEATS_PER_ROW = len(SEAT_LETTERS)
SKIPPED_ROW = 13


def generate_seat_letters(number):
    for seat_index in range(number):
        yield SEAT_LETTERS[seat_index % SEATS_PER_ROW]


def generate_seats(number):
    seats_generated = 0
    for row_number in _generate_row_numbers():
        for seat in _generate_row_seats(row_number):
            if seats_generated >= number:
                return

            yield seat
            seats_generated += 1


def _generate_row_numbers():
    row_number = 1
    while True:
        if row_number != SKIPPED_ROW:
            yield row_number

        row_number += 1


def _generate_row_seats(row_number):
    for seat_letter in generate_seat_letters(SEATS_PER_ROW):
        yield f"{row_number}{seat_letter}"


def assign_seats(passengers):
    passenger_count = len(passengers)
    passengers_with_seats = zip(passengers, generate_seats(passenger_count))
    return dict(passengers_with_seats)


def generate_codes(seat_numbers, flight_id):
    for seat_number in seat_numbers:
        ticket_number = f"{seat_number}{flight_id}"
        yield ticket_number.ljust(12, "0")