from typing import NamedTuple


class MatchedRecord(NamedTuple):
    treasure_name: str
    treasure_coordinate: str
    location_name: str
    location_coordinate: tuple[str, str]
    quadrant: str