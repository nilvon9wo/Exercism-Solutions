type t = {
    row : int;
    column : int;
}

let create ~row ~column = {
    row;
    column;
}

let is_center_offset (delta_row, delta_column) =
    delta_row = 0 && delta_column = 0

let is_valid_offset offset =
    not (is_center_offset offset)

let apply_offset_to_coordinate coordinate (delta_row, delta_column) =
    let row = coordinate.row in
    let column = coordinate.column in
    create ~row:(row + delta_row) ~column:(column + delta_column)

let neighbors (coordinate : t) : t list =
    let offsets = [ -1; 0; 1 ] in
    Base.List.cartesian_product offsets offsets
    |> Base.List.filter ~f:is_valid_offset
    |> Base.List.map ~f:(apply_offset_to_coordinate coordinate)
