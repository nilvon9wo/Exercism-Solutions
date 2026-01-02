open Rectangle_candidate
open Count_context
open Row_pair

type left_scan_state = {
    context           : count_context;
    row_pair          : row_pair;
    accumulated_count : int;
    remaining_columns : int list;
}
