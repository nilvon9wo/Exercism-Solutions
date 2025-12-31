open Base
open Int64

type prime_factors_state = {
  number: Int64.t;
  candidate: Int64.t;
  accumulated_factors: Int64.t list;
}

let is_divisor number candidate =
  let remainder = rem number candidate in
  remainder = 0L

let get_next_candidate candidate =
  if candidate = 2L
      then 3L
      else candidate + 2L

let compute_next_state state =
    let { number; candidate; accumulated_factors } = state in
    if is_divisor number candidate
        then {
            state with
            number = number / candidate;
            accumulated_factors = candidate :: accumulated_factors;
        }
        else {
            state with
            candidate = get_next_candidate candidate;
        }