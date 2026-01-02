open Row_with_columns

type row_pair_with_columns = {
  top: row_with_columns;
  bottom: row_with_columns;
}

let create top bottom = {
    top;
    bottom
}

