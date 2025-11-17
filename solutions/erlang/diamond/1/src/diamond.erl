-module(diamond).

-export([rows/1]).

rows(Letter) ->
  TopRows = make_top_rows(Letter),
  BottomRows = make_bottom_rows(Letter),
  lists:append([TopRows, BottomRows]).

make_top_rows(LastLetter)
  when (LastLetter == "A") ->
  ["A"];

make_top_rows(LastLetter) ->
  Letters = lists:seq(hd("A"), hd(LastLetter)),
  lists:map(make_row(LastLetter), Letters).

make_bottom_rows(LastLetter)
  when (LastLetter == "A") ->
  [];

make_bottom_rows(LastLetter) ->
  Letters = lists:seq(hd("A"), hd(LastLetter) - 1),
  lists:map(make_row(LastLetter), lists:reverse(Letters)).

make_row(LastLetter) ->
  fun(CurrentLetter) ->
    OuterSpaces = create_outer_spaces(hd(LastLetter) - CurrentLetter),
    InnerSpaces = create_inner_spaces(CurrentLetter - hd("A")),
    Row = create_row(OuterSpaces, CurrentLetter, InnerSpaces),
    unicode:characters_to_list(Row)
  end.

create_row(OuterSpaces, CurrentLetter, _InnerSpaces)
  when (CurrentLetter == hd("A")) ->
  [
    OuterSpaces,
    CurrentLetter,
    OuterSpaces
  ];

create_row(OuterSpaces, CurrentLetter, InnerSpaces) ->
  [
    OuterSpaces,
    CurrentLetter,
    InnerSpaces,
    CurrentLetter,
    OuterSpaces
  ].

create_outer_spaces(DistanceFromA) ->
  SpacesNeeded = lists:seq(1, DistanceFromA),
  create_spaces(SpacesNeeded).

create_inner_spaces(DistanceFromA)
  when (DistanceFromA == 0) ->
  "";

create_inner_spaces(DistanceFromA)
  when (DistanceFromA == 1) ->
  " ";

create_inner_spaces(DistanceFromA) ->
  SpacesNeeded = lists:seq(1, (DistanceFromA * 2) - 1),
  create_spaces(SpacesNeeded).

create_spaces(SpacesNeeded) ->
  Spaces = lists:map(fun(_X) -> " " end, SpacesNeeded),
  unicode:characters_to_binary(Spaces).

