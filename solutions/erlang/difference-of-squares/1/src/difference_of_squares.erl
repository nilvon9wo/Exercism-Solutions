-module(difference_of_squares).

-export([difference_of_squares/1, square_of_sum/1, sum_of_squares/1]).

difference_of_squares(Number) ->
  square_of_sum(Number) - sum_of_squares(Number).

square_of_sum(Number) ->
  square_of_sum(Number, 0).

square_of_sum(Number, Accumulated) when Number > 0 ->
  square_of_sum(Number - 1, Accumulated + Number);

square_of_sum(_Number, Accumulated) ->
  Accumulated * Accumulated.

sum_of_squares(Number) ->
  sum_of_squares(Number, 0).

sum_of_squares(Number, Accumulated) when Number > 0 ->
  sum_of_squares(Number - 1, Accumulated + Number * Number);

sum_of_squares(_Number, Accumulated) ->
  Accumulated.
