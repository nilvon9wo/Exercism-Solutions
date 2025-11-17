-module(prime_factors).

-export([factors/1]).


factors(Value)
  when Value < 2
  -> [];

factors(Value) ->
  OddPotentialFactors = lists:seq(3, trunc(Value / 2) + 1, 2),
  find_prime_factors([2 | OddPotentialFactors], Value).

find_prime_factors(PotentialFactors, Value) ->
  find_prime_factors(PotentialFactors, Value, _Accumulator = []).

find_prime_factors(PotentialFactors, Value, Accumulator)
  when length(PotentialFactors) == 0 andalso
    length(Accumulator) == 0 ->
  [Value];

find_prime_factors(PotentialFactors, _Value, Accumulator)
  when length(PotentialFactors) == 0 ->
  Accumulator;

find_prime_factors(PotentialFactors, Value, Accumulator) ->
  [Head | Tail] = PotentialFactors,
  if
    (Value rem Head == 0) ->
      find_prime_factors(PotentialFactors, trunc(Value / Head), [ Head | Accumulator ]);

    true ->
      find_prime_factors(Tail, Value, Accumulator)
  end.
