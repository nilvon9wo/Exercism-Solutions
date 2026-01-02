open Column_pair
open Count_context
open Row_pair

type right_scan_state = {
    context          : count_context;
    row_pair         : row_pair;
    column_pair      : column_pair;
    accumulated_count: int;
}
