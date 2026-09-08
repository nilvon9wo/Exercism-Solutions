from location_record import LocationRecord
from matched_record import MatchedRecord
from treasure_record import TreasureRecord


def get_coordinate(treasure_record):
    treasure = TreasureRecord(*treasure_record)
    return treasure.coordinate


def convert_coordinate(coordinate):
    row, column = coordinate
    return row, column


def compare_records(treasure_record, location_record):
    treasure = TreasureRecord(*treasure_record)
    location = LocationRecord(*location_record)

    return convert_coordinate(treasure.coordinate) == location.coordinate


def create_record(treasure_record, location_record):
    treasure = TreasureRecord(*treasure_record)
    location = LocationRecord(*location_record)

    if not compare_records(treasure, location):
        return "not a match"

    return MatchedRecord(
        treasure.name,
        treasure.coordinate,
        location.name,
        location.coordinate,
        location.quadrant,
    )


def clean_up(records):
    report = ""

    for record in records:
        report += _format_cleaned_record(record)

    return report


def _format_cleaned_record(record) -> str:
    matched_record = MatchedRecord(*record)

    cleaned_record = (
        matched_record.treasure_name,
        matched_record.location_name,
        matched_record.location_coordinate,
        matched_record.quadrant,
    )

    return f"{cleaned_record}\n"
