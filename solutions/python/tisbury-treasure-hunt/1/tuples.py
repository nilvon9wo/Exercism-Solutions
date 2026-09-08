from typing import NamedTuple


class TreasureRecord(NamedTuple):
    name: str
    coordinate: str


class LocationRecord(NamedTuple):
    name: str
    coordinate: tuple[str, str]
    quadrant: str


class MatchedRecord(NamedTuple):
    treasure_name: str
    treasure_coordinate: str
    location_name: str
    location_coordinate: tuple[str, str]
    quadrant: str


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

    return (
        treasure.name,
        treasure.coordinate,
        location.name,
        location.coordinate,
        location.quadrant,
    )


def clean_up(records):
    report = ""

    for record in records:
        treasure_name, _, location_name, location_coordinate, quadrant = record
        cleaned_record = (
            treasure_name,
            location_name,
            location_coordinate,
            quadrant,
        )
        report += f"{cleaned_record}\n"

    return report