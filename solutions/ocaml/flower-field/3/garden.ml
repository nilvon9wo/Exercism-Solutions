open Base

type t = {
    rows : string array;
    width : int;
    height : int;
}

let find_width lines =
    match lines with
    | [] -> 0
    | first :: _ -> String.length first

let from_strings (lines : string list) : t = {
        rows = Array.of_list lines;
        width = find_width lines;
        height = List.length lines;
    }

let is_inside (garden : t) (coordinate : Coordinate.t) : bool =
    let row = coordinate.row in
    let column = coordinate.column in
    row >= 0
        && row < garden.height
        && column >= 0
        && column < garden.width

let cell_at (garden : t) (coordinate : Coordinate.t) : char option =
    if is_inside garden coordinate
        then Some garden.rows.(coordinate.row).[coordinate.column]
        else None

let is_flower_at ~(garden : t) ~(coordinate : Coordinate.t) : bool =
    match cell_at garden coordinate with
    | Some '*' -> true
    | _ -> false