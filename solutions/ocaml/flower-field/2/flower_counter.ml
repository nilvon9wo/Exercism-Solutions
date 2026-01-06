let is_flower_at ~(garden : Garden.t) ~(coordinate : Coordinate.t) : bool =
    match Garden.cell_at garden coordinate with
    | Some '*' -> true
    | _ -> false

let count_adjacent_flowers ~(garden : Garden.t) ~(coordinate : Coordinate.t) : int =
    coordinate
    |> Coordinate.neighbors
    |> Base.List.filter ~f:(Garden.is_inside garden)
    |> Base.List.count ~f:(fun coordinate -> is_flower_at ~garden ~coordinate)
