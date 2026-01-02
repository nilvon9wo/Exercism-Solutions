open Rectangle_candidate
open Count_context
open Row_pair

type right_scan_state = {
    context          : count_context;
    row_pair         : row_pair;
    rectangle_candidate      : rectangle_candidate;
    accumulated_count: int;
}
