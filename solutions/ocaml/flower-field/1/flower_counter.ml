let is_flower_at ~(field : Field.t) ~(coordinate : Coordinate.t) : bool =
    match Field.cell_at field coordinate with
    | Some '*' -> true
    | _ -> false

let count_adjacent_flowers ~(field : Field.t) ~(coordinate : Coordinate.t) : int =
    coordinate
    |> Coordinate.neighbors
    |> Base.List.filter ~f:(fun coordinate -> Field.is_inside field coordinate)
    |> Base.List.count ~f:(fun coordinate -> is_flower_at ~field ~coordinate)
