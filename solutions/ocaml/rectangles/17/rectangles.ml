open Count_context
open Grid
open Position
open Rectangle
open Rectangle_counter
open Rectangle_search
open Row_with_corners

let rows_with_corner_columns grid =
  let rows = rows_with_corner_positions grid in
  List.map (fun row -> (row.row_index, List.map (fun pos -> pos.column) row.positions)) rows

let count_all_rectangles grid =
  let rows = rows_with_corner_columns grid in
  let context = { validator = (fun top left ->
      let rectangle = Rectangle.create top left in
      is_complete rectangle grid
  )} in
  count context rows

let count_rectangles grid_rows =
  let grid = Grid.create grid_rows in
  if grid.row_count = 0 || grid.column_count = 0
  then 0
  else count_all_rectangles grid
