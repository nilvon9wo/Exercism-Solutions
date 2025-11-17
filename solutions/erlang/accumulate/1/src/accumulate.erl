-module(accumulate).

-export([accumulate/2]).

accumulate(Function, List) ->
  accumulate(Function, List, _Accumulated = []).

accumulate(_Function, List, Accumulated)
  when length(List) == 0 ->
  lists:reverse(Accumulated);

accumulate(Function, [Head | Tail], Accumulated) ->
  accumulate(Function, Tail, [Function(Head) | Accumulated]).