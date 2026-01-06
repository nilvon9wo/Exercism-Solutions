let count_adjacent_flowers ~(garden : Garden.t) ~(coordinate : Coordinate.t) : int =
    coordinate
    |> Coordinate.neighbors
    |> Base.List.filter ~f:(Garden.is_inside garden)
    |> Base.List.count ~f:(fun coordinate -> Garden.is_flower_at ~garden ~coordinate)
