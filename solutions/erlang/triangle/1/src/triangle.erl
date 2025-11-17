-module(triangle).

-export([kind/3]).


kind(A, B, C)
  when (A == 0) or (B == 0) or (C == 0) ->
  {error, "all side lengths must be positive"};

kind(A, B, C)
  when (A == B) and (A == C) and (B == C) ->
  equilateral;

kind(A, B, C)
  when (A > (B + C))
  or (B > (A + C))
  or (C > (A + B)) ->
  {error, "side lengths violate triangle inequality"};

kind(A, B, C)
  when ((A == B) and (C == 2 * A))
  or ((A == C) and (B == 2 * A))
  or ((B == C) and (A == 2 * B)) ->
  degenerate;

kind(A, B, C)
  when (A == B)
  or (A == C)
  or (B == C) ->
  isosceles;

kind(_A, _B, _C) ->
  scalene.
