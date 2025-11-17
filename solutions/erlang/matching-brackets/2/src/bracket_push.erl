-module(bracket_push).

-export([is_paired/1]).

-define(BRACKETS_MAP, #{
  "[" => "]",
  "{" => "}",
  "(" => ")"
}).

-define(OPENERS, maps:keys(?BRACKETS_MAP)).
-define(CLOSERS, maps:values(?BRACKETS_MAP)).

is_paired(String) ->
  Openers = [],
  is_paired(String, Openers).

is_paired(String, Openers)
  when (length(String) == 0) ->
  length(Openers) == 0;

is_paired(String, Openers) ->
  [Head | Tail] = String,
  Character = unicode:characters_to_list([Head]),
  Role = determine_role(Character),
  is_paired(Character, Tail, Openers, Role).

is_paired(Character, Tail, Openers, opener) ->
  is_paired(Tail, [Character | Openers]);

is_paired(_Character, _Tail, Openers, closer)
  when (length(Openers) == 0) ->
  false;

is_paired(Character, Tail, Openers, closer) ->
  [LastOpener | RemainingOpeners] = Openers,
  RequiredCloser = maps:get(LastOpener, ?BRACKETS_MAP),
  IsMatchingCloser = Character == RequiredCloser,
  if
    IsMatchingCloser ->
      is_paired(Tail, RemainingOpeners);

    true ->
      false
  end;

is_paired(_Character, Tail, Openers, other) ->
  is_paired(Tail, Openers).

determine_role(Character) ->
  IsOpener = lists:member(Character, ?OPENERS),
  IsCloser = lists:member(Character, ?CLOSERS),
  if
    IsOpener -> opener;
    IsCloser -> closer;
    true -> other
  end.







