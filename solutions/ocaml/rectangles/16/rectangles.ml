open Grid
open Position
open Rectangle
open Rectangle_search
open Row_with_corners
open Rectangle_counter

(* Convert Row_with_corners to simple corner column lists *)
let rows_with_corner_columns grid =
  let rows = rows_with_corner_positions grid in
  List.map
    (fun row ->
      ( row.row_index,
        List.map (fun pos -> pos.column) row.positions ))
    rows

let validate_rectangle grid top_left bottom_right =
  let rectangle =
    Rectangle.create top_left bottom_right
  in
  is_complete rectangle grid

let count_all_rectangles grid =
  let rows =
    rows_with_corner_columns grid
  in
  let validator =
    validate_rectangle grid
  in
  Rectangle_counter.count validator rows

let count_rectangles grid_rows =
  let grid = Grid.create grid_rows in
  if grid.row_count = 0 || grid.column_count = 0
  then 0
  else count_all_rectangles grid
