open Base
open Int64
open Prime_factors_state

let rec factor_number state =
  if state.number = 1L
      then List.rev state.accumulated_factors
      else factor_number (compute_next_state state)

let factors_of number =
  if number <= 1L
      then []
      else factor_number {
        number;
        candidate = 2L;
        accumulated_factors = []
      }
