open Grid
open Position

type rectangle = {
    top_left : position;
    bottom_right : position;
}

let create top_left bottom_right = {
    top_left;
    bottom_right;
}

let top_row rectangle =
    rectangle.top_left.row

let bottom_row rectangle =
    rectangle.bottom_right.row

let left_column rectangle =
    rectangle.top_left.column

let right_column rectangle =
    rectangle.bottom_right.column

let has_positive_area rectangle =
    top_row rectangle < bottom_row rectangle
        && left_column rectangle < right_column rectangle

let rec is_horizontal_edge_range grid row column_start column_end =
  if column_start > column_end
      then true
      else
            let current_character = char_at grid { row; column = column_start } in
            match current_character with
            | '+' | '-' ->
                let next_column = column_start + 1 in
                is_horizontal_edge_range grid row next_column column_end
            | _ ->
                false

let is_horizontal_edge grid row left_column right_column =
    is_horizontal_edge_range grid row left_column right_column

let rec is_vertical_edge_range grid column row_start row_end =
  if row_start > row_end
      then true
      else
            let current_character = char_at grid { row = row_start; column } in
            let next_row = (row_start + 1) in
            match current_character with
            | '+' | '|' -> is_vertical_edge_range grid column next_row row_end
            | _ -> false

let is_vertical_edge grid column top_row bottom_row =
  is_vertical_edge_range grid column top_row bottom_row

let is_complete rectangle grid =
     let top_row_index = top_row rectangle in
     let left_column_index = left_column rectangle in
     let right_column_index = right_column rectangle in
     let bottom_row_index = bottom_row rectangle in
     has_positive_area rectangle
          && is_horizontal_edge grid top_row_index left_column_index right_column_index
          && is_horizontal_edge grid bottom_row_index left_column_index right_column_index
          && is_vertical_edge grid left_column_index top_row_index bottom_row_index
          && is_vertical_edge grid right_column_index top_row_index bottom_row_index
