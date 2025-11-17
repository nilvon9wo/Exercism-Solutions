-module(isogram).

-export([is_isogram/1]).


is_isogram(Phrase) ->
  LowercasePhrase = string:lowercase(Phrase),
  CleanPhrase = strip_characters(LowercasePhrase, "- "),

  lists:sort(CleanPhrase) == lists:usort(CleanPhrase).

%% https://www.rosettacode.org/wiki/Strip_a_set_of_characters_from_a_string#Erlang
strip_characters(ToStrip, StripOut) ->
  lists:filter(is_character_allowed(StripOut), ToStrip).

is_character_allowed(StripOut) ->
  fun(Character) ->
    not lists:member(Character, StripOut)
  end.