open Position

type grid = {
  rows : string array;
  row_count : int;
  column_count : int;
}

let create (rows : string array) : grid =
  let row_count = Array.length rows in
  {
    rows;
    row_count;
    column_count = if row_count = 0
                           then 0
                           else String.length rows.(0)
  }

let is_within_bounds (grid : grid) (position : position) : bool =
  position.row >= 0
      && position.row < grid.row_count
      && position.column >= 0
      && position.column < grid.column_count

let char_at (grid : grid) (position : position) : char =
  grid.rows.(position.row).[position.column]

let is_corner (grid : grid) (position : position) : bool =
    position
    |> char_at grid
    |> Char.equal '+'
