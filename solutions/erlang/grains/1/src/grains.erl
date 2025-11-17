-module(grains).

-export([square/1, total/0]).

square(Square)
  when (Square < 1) or (Square > 64) ->
  {error, "square must be between 1 and 64"};

square(Square) ->
  round(math:pow(2, Square - 1)).

total() ->
  Squares = lists:seq(1, 64),
  Grains = lists:map(collect_grain(), Squares),
  lists:foldl(add_grain(), _Accumulator = 0, Grains).

add_grain() ->
  fun(Grain, Accumulator) ->
    Accumulator + Grain
  end.

collect_grain() ->
  fun(Square) ->
    square(Square)
  end.



