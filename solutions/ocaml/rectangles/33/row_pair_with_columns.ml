open Row_with_columns

type row_pair_with_columns = {
  top: row_with_columns;
  bottom: row_with_columns;
}

let create top bottom = {
    top;
    bottom
}

let row_pair_with_columns_from_top_and_bottom top_row_with_columns bottom_row bottom_columns =
    let bottom_row_with_columns = Row_with_columns.create bottom_row bottom_columns in
    create top_row_with_columns bottom_row_with_columns
