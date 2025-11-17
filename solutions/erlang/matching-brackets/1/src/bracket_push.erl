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
  IsOpener = lists:member(Character, ?OPENERS),

  if
    IsOpener ->
      handle_opener(Character, Tail, Openers);

    true ->
      handle_non_opener(Character, Tail, Openers)
  end.

handle_opener(Character, Tail, Openers) ->
  is_paired(Tail, [Character | Openers]).

handle_non_opener(Character, Tail, Openers) ->
  IsCloser = lists:member(Character, ?CLOSERS),
  if
    IsCloser ->
      handle_closer(Character, Tail, Openers);

    true ->
      handle_noise(Tail, Openers)
  end.

handle_closer(_Character, _Tail, Openers)
  when (length(Openers) == 0) ->
  false;

handle_closer(Character, Tail, Openers) ->
  [LastOpener | RemainingOpeners] = Openers,
  RequiredCloser = maps:get(LastOpener, ?BRACKETS_MAP),
  IsMatchingCloser = Character == RequiredCloser,
  if
    IsMatchingCloser ->
      is_paired(Tail, RemainingOpeners);

    true ->
      false
  end.

handle_noise(Tail, Openers) ->
  is_paired(Tail, Openers).




