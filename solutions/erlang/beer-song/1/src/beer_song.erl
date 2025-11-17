-module(beer_song).

-export([verse/1, sing/1, sing/2]).

-define(STARTING_COUNT, 99).
-define(PENULTIMATE_BOTTLE, 2).
-define(FINAL_BOTTLE, 1).
-define(NO_MORE_BOTTLES, 0).
-define(REPLACE_BOTTLES, -1).

verse(?NO_MORE_BOTTLES) ->
  BasicVerse = basicVerse(?NO_MORE_BOTTLES),
  FixedVerse = lists:flatten(string:replace(
    BasicVerse,
    "Take one down and pass it around,", "Go to the store and buy some more,"
  )),
  declareNoBottles(FixedVerse);

verse(?FINAL_BOTTLE) ->
  BasicVerse = basicVerse(?FINAL_BOTTLE),
  FixedVerse = lists:flatten(string:replace(BasicVerse, "Take one", "Take it")),
  SingularVerse = singularizeLastBottle(FixedVerse),
  declareNoBottles(SingularVerse);

verse(?PENULTIMATE_BOTTLE) ->
  BasicVerse = basicVerse(?PENULTIMATE_BOTTLE),
  singularizeLastBottle(BasicVerse);

verse(Integer) ->
  basicVerse(Integer).

basicVerse(Integer) ->
  integer_to_list(Integer) ++ " bottles of beer on the wall, " ++ integer_to_list(Integer) ++ " bottles of beer.\n" ++
    "Take one down and pass it around, " ++ calculateAfterPassing(Integer) ++ " bottles of beer on the wall.\n".

singularizeLastBottle(Verse) ->
  string:replace(Verse, "1 bottles", "1 bottle", all).

declareNoBottles(Verse) ->
  ReplacedVerse = string:replace(Verse, "0 bottles", "No more bottles", all),
  string:replace(ReplacedVerse, " No", " no").

calculateAfterPassing(?NO_MORE_BOTTLES) ->
  integer_to_list(?STARTING_COUNT);

calculateAfterPassing(Integer) ->
  integer_to_list(Integer - 1).

sing(From) ->
  sing(From, ?NO_MORE_BOTTLES).

sing(From, To) ->
  Range = lists:seq(From, To, -1),
  lists:foldl(fun addVerse/2, [], Range).

addVerse(Bottles, PastVerses) ->
  PastVerses ++ verse(Bottles) ++ "\n".