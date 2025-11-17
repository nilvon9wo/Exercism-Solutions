-module(sieve).

-export([primes/1]).

-define(LOWER_LIMIT, 2).
-define(COULD_BE_PRIME, true).
-define(NOT_PRIME, false).

primes(UpperLimit)
  when UpperLimit < ?LOWER_LIMIT ->
  [];

primes(UpperLimit) ->
  Range = lists:seq(?LOWER_LIMIT, UpperLimit),
  MarkedMap = create_marked_range(Range, ?COULD_BE_PRIME),
  PrimeMarkedMap = mark_primes(?LOWER_LIMIT, UpperLimit, MarkedMap),
  FilteredMap = could_be_prime(PrimeMarkedMap),
  maps:keys(FilteredMap).

mark_primes(nil, _UpperLimit, MarkedRange) ->
  MarkedRange;

mark_primes(CurrentValue, UpperLimit, OldMarkedMap) ->
  Disqualified = lists:seq(CurrentValue * 2, UpperLimit, CurrentValue),
  NewMarkedMap = create_marked_range(Disqualified, ?NOT_PRIME),
  MergedMarkedMap = maps:merge(OldMarkedMap, NewMarkedMap),
  NextValue = find_next_prime(CurrentValue, MergedMarkedMap),
  mark_primes(NextValue, UpperLimit, MergedMarkedMap).

create_marked_range(Range, Value) ->
  RangeMap = lists:map(fun(X) -> {X, Value} end, Range),
  maps:from_list(RangeMap).

could_be_prime(MarkedMap) ->
  maps:filter(fun could_be_prime/2, MarkedMap).

could_be_prime(_X, IsPrime) ->
  IsPrime.

find_next_prime(CurrentValue, MergedMarkedMap) ->
  PossiblePrimesMap = could_be_prime(MergedMarkedMap),
  PossiblePrimes = maps:keys(PossiblePrimesMap),
  LargerPossiblePrimes = lists:filter(fun(X) -> X > CurrentValue end, PossiblePrimes),
  find_next_prime(LargerPossiblePrimes).

find_next_prime([] = _LargerPossiblePrimes) ->
  nil;

find_next_prime(LargerPossiblePrimes) ->
  lists:min(LargerPossiblePrimes).
