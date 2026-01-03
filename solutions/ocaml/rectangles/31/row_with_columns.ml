type row_with_columns = {
  row_index: int;
  columns: int list;
}

let create row_index columns = {
    row_index;
    columns
}