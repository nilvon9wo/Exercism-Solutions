-module(queen_attack).

-export([can_attack/2]).

can_attack({WhiteQueenX, _WhiteQueenY}, {BlackQueenX, _BlackQueenY})
  when (WhiteQueenX == BlackQueenX) ->
  true;

can_attack({_WhiteQueenX, WhiteQueenY}, {_BlackQueenX, BlackQueenY})
  when (WhiteQueenY == BlackQueenY) ->
  true;

can_attack({WhiteQueenX, WhiteQueenY}, {BlackQueenX, BlackQueenY}) ->
  DifferenceX = WhiteQueenX - BlackQueenX,
  DifferenceY = WhiteQueenY - BlackQueenY,
  abs(DifferenceX) == abs(DifferenceY).

