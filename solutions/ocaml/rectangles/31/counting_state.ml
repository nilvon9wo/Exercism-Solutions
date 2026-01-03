open Count_context
open Rectangle_candidate
open Row_pair

type counting_state = {
  context: count_context;
  row_pair: row_pair;
  candidate: rectangle_candidate;
}
