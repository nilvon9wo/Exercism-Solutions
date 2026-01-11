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

let parse_row (line : string) : Player.t option array =
  line
  |> String.strip
  |> String.split ~on:' '
  |> List.filter ~f:(fun s -> not (String.is_empty s))
  |> List.map ~f:parse_cell
  |> Array.of_list

let calculate_rows (lines : string list) : Player.t option array array =
  lines
  |> List.map ~f:parse_row
  |> Array.of_list

let from_strings (lines : string list) : t =
  let rows = calculate_rows lines in
  let height = Array.length rows in
  let width = if height = 0 then 0 else Array.length rows.(0) in
  { rows; width; height }

(* --- Accessors --- *)

let width (board : t) : int = board.width
let height (board : t) : int = board.height

(* --- Coordinate helpers --- *)

(* Construct a Coordinate.t from board row/column (for connectivity.ml) *)
let coordinate_at_row_column (_board : t) ~row ~column : Coordinate.t =
  Coordinate.create ~x:column ~y:row ~z:(-(row + column))

(* Get the row index from a Coordinate.t *)
let row_of_coordinate (coord : Coordinate.t) : int = coord.y

(* Get the column index from a Coordinate.t *)
let column_of_coordinate (coord : Coordinate.t) : int = coord.x

(* --- Board queries --- *)

let is_inside (board : t) (coord : Coordinate.t) : bool =
  let row = row_of_coordinate coord in
  let col = column_of_coordinate coord in
  row >= 0 && row < board.height && col >= 0 && col < Array.length board.rows.(row)

let cell_at (board : t) (coord : Coordinate.t) : Player.t option =
  if is_inside board coord then
    board.rows.(row_of_coordinate coord).(column_of_coordinate coord)
  else
    None
