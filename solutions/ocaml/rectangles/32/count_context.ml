open Position
open Row_with_columns

type count_context = {
  rows: row_with_columns list;
  validator: position -> position -> bool;
}