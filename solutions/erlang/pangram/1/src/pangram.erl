-module(pangram).

-export([is_pangram/1]).

is_pangram(Sentence) ->
  UpperCaseSentence = string:uppercase(Sentence),
  ResultMap = create_result_map(UpperCaseSentence),
  evaluate_results(ResultMap).

create_result_map(Sentence) ->
  ResultList = lists:map(create_result(), Sentence),
  maps:from_list(ResultList).

create_result() ->
  fun(Character) ->
    {Character, true}
  end.

evaluate_results(ResultMap) ->
  Alphabet = create_alphabet(),
  ResultList = lists:map(evaluate_letter(ResultMap), Alphabet),
  lists:all(is_true(), ResultList).

evaluate_letter(ResultMap) ->
  fun(Letter) ->
    maps:get(Letter, ResultMap, false)
  end.

is_true() ->
  fun(Result) ->
    Result
  end.

create_alphabet() ->
  lists:seq(hd("A"), hd("Z")).