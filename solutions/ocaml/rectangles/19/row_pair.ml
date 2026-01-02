open Row_index

type row_pair = {
  top_row: row_index;
  bottom_row: row_index;
}

let create top_row bottom_row = {
    top_row;
    bottom_row
}
