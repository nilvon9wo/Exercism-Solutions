open Base

type t = {
  rows : Player.t option array array;
  width : int;
  height : int;
}

(* --- Parsing --- *)

let parse_cell = function
  | "X" -> Some Player.X
  | "O" -> Some Player.O
  | "." -> None
  | _ -> None

let is_non_empty string =
  not (String.is_empty string)

let parse_row (line : string) : Player.t option array =
  line
  |> String.strip
  |> String.split ~on:' '
  |> List.filter ~f:is_non_empty
  |> List.map ~f:parse_cell
  |> Array.of_list

let calculate_rows (lines : string list) : Player.t option array array =
  lines
  |> List.map ~f:parse_row
  |> Array.of_list

let calculate_width rows height =
    if height = 0
        then 0
        else Array.length rows.(0)

let from_strings (lines : string list) : t =
      let rows = calculate_rows lines in
      let height = Array.length rows in
      {
            rows;
            width = calculate_width rows height;
            height
      }

(* --- Coordinate helpers --- *)
let coordinate_at_xy (_board : t) ~y ~x : Coordinate.t =
    let z = -y - x in
  Coordinate.create ~x ~y ~z

(* --- Board queries --- *)

let is_inside (board : t) (coordinate : Coordinate.t) : bool =
  let y = coordinate.y in
  let x = coordinate.x in
  y >= 0
    && y < board.height
    && x >= 0
    && x < Array.length board.rows.(y)

let cell_at (board : t) (coordinate : Coordinate.t) : Player.t option =
  if is_inside board coordinate
    then board.rows.(coordinate.y).(coordinate.x)
    else None