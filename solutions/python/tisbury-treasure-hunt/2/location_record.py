from typing import NamedTuple


class LocationRecord(NamedTuple):
    name: str
    coordinate: tuple[str, str]
    quadrant: str