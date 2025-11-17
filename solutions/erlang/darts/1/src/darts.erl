-module(darts).

-export([score/2]).


score(X, Y) ->
  score(convert_to_polar(X, Y)).

score({Radius, _Theta})
  when (Radius == 0) ->
  10;

score({Radius, _Theta})
  when (Radius =< 5) ->
  5;

score({Radius, _Theta})
  when (Radius =< 10) ->
  1;

score({_Radius, _Theta}) ->
  0.


convert_to_polar(X, Y) ->
  Radius = find_radius(X, Y),
  Theta = unused,
  {Radius, Theta}.

find_radius(X, Y) ->
  math:sqrt((X * X) + (Y * Y)).

