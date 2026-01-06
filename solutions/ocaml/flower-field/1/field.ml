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

let from_strings (lines : string list) : t =
    {
        rows = Array.of_list lines;
        width = find_width lines;
        height = List.length lines;
    }

let width (field : t) : int =
    field.width

let height (field : t) : int =
    field.height

let is_inside (field : t) (coordinate : Coordinate.t) : bool =
    coordinate.row >= 0
        && coordinate.row < field.height
        && coordinate.column >= 0
        && coordinate.column < field.width

let cell_at (field : t) (coordinate : Coordinate.t) : char option =
    if is_inside field coordinate
        then Some field.rows.(coordinate.row).[coordinate.column]
        else None

let all_coordinates (field : t) : Coordinate.t list =
    let rec collect row column accumulator =
        if row >= field.height
            then accumulator
            else if column >= field.width
                    then collect (row+1) 0 accumulator
                    else collect row (column+1) (Coordinate.create ~row ~column:column :: accumulator)
    in
    List.rev (collect 0 0 [])
