def get_list_of_wagons(*wagon_ids):
    return list(wagon_ids)


def fix_list_of_wagons(wagons, missing_wagons):
    first_wagon, second_wagon, locomotive, *remaining_wagons = wagons

    return [
        locomotive,
        *missing_wagons,
        *remaining_wagons,
        first_wagon,
        second_wagon,
    ]


def add_missing_stops(route, **stops):
    route["stops"] = list(stops.values())

    return route


def extend_route_information(route, additional_information):
    return {**route, **additional_information}


def fix_wagon_depot(wagon_depot):
    first_row, second_row, third_row = wagon_depot

    first_wagon, second_wagon, third_wagon = first_row
    fourth_wagon, fifth_wagon, sixth_wagon = second_row
    seventh_wagon, eighth_wagon, ninth_wagon = third_row

    return [
        [first_wagon, fourth_wagon, seventh_wagon],
        [second_wagon, fifth_wagon, eighth_wagon],
        [third_wagon, sixth_wagon, ninth_wagon],
    ]