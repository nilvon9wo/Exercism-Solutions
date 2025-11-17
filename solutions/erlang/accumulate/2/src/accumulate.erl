-module(accumulate).

-export([accumulate/2]).

accumulate(Function, List) ->
  accumulate(Function, List, _Accumulated = []).

accumulate(_Function, [], Accumulated) ->
  lists:reverse(Accumulated);

accumulate(Function, [Head | Tail], Accumulated) ->
  accumulate(Function, Tail, [Function(Head) | Accumulated]).