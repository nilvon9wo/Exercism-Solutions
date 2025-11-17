-module(change).

-export([find_fewest_coins/2]).

-define(ERROR, {error, "cannot change"}).

find_fewest_coins(Target, Coins)
  when Target < 0
  -> erlang:error(Target, Coins);

find_fewest_coins(Target, _Coins)
  when Target == 0
  -> [];

find_fewest_coins(Target, [SmallestCoin | _MoreCoins])
  when Target < SmallestCoin
  -> undefined;

find_fewest_coins(Target, Coins)
  ->
  case find_fewest_coins(Target, lists:reverse(Coins), _Accumulator = []) of
    {ok, FewestCoins} -> FewestCoins;
    {error, _} -> undefined
  end.

find_fewest_coins(Target, Coins, _Accumulator)
  when (Target =/= 0) andalso (length(Coins)) == 0
  -> ?ERROR;

find_fewest_coins(_Target, Coins, Accumulator)
  when length(Coins) == 0
  -> {ok, Accumulator};

find_fewest_coins(Target, [LargestCoin | MoreCoins], Accumulator)
  when LargestCoin > Target
  -> find_fewest_coins(Target, MoreCoins, Accumulator);

find_fewest_coins(Target, [LargestCoin | _MoreCoins] = Coins, Accumulator) ->
  ObviousResult = {ObviousStatus, ObviousCollection} =
    find_fewest_coins(Target - LargestCoin, Coins, [LargestCoin | Accumulator]),

  if
    (ObviousStatus == ok)
      and (length(ObviousCollection) == 1) ->
      {ok, ObviousCollection};

    true ->
      find_fewest_coins_alternatives(Target, Coins, Accumulator, ObviousResult)
  end.

find_fewest_coins_alternatives(Target, [_LargestCoin | MoreCoins], Accumulator, {error, _})
  -> find_fewest_coins(Target, MoreCoins, Accumulator);

find_fewest_coins_alternatives(Target, [LargestCoin | MoreCoins], Accumulator, {ok, ObviousCollection})
  ->
  AvailableCoins = lists:dropwhile(fun(Coin) -> LargestCoin rem Coin == 0 end, MoreCoins),
  {AlternativeStatus, AlternativeCollection} = find_fewest_coins(Target, AvailableCoins, Accumulator),

  NoAlternative = AlternativeStatus == error,
  AlternativeTooBig = length(AlternativeCollection) >= length(ObviousCollection),
  if
    NoAlternative or AlternativeTooBig ->
      {ok, ObviousCollection};

    true ->
      {ok, AlternativeCollection}
  end.
