open Count_context
open Row_with_columns

type row_pair_counting_context = {
    context : count_context;
    top_row : row_with_columns;
}
