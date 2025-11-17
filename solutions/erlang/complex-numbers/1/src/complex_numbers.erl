-module(complex_numbers).

-export([abs/1, add/2, conjugate/1, divide/2, equal/2, exp/1, imaginary/1, mul/2, new/2,
  real/1, sub/2]).

-record(complex_number, {real_part, imaginary_part}).

-tolerance(0.005).

abs({complex_number, RealPart, ImaginaryPart1}) ->
  math:sqrt(math:pow(RealPart, 2) + math:pow(ImaginaryPart1, 2)).

add(
    {complex_number, RealPart1, ImaginaryPart1},
    {complex_number, RealPart2, ImaginaryPart2}
) ->
  new(RealPart1 + RealPart2, ImaginaryPart1 + ImaginaryPart2).

conjugate({complex_number, RealPart, ImaginaryPart}) ->
  new(RealPart, -ImaginaryPart).

divide(
    {complex_number, RealPart1, ImaginaryPart1},
    {complex_number, RealPart2, ImaginaryPart2}
) ->
  new(
    (RealPart1 * RealPart2 + ImaginaryPart1 * ImaginaryPart2) / (math:pow(RealPart2, 2) + math:pow(ImaginaryPart2, 2)),
    (RealPart2 * ImaginaryPart1 - RealPart1 * ImaginaryPart2) / (math:pow(RealPart2, 2) + math:pow(ImaginaryPart2, 2))
  ).

equal(
    {complex_number, RealPart1, ImaginaryPart1},
    {complex_number, RealPart2, ImaginaryPart2}
) ->
  {complex_number, RealPartDifference, ImaginaryDifference} =
    new(RealPart1 - RealPart2, ImaginaryPart1 - ImaginaryPart2),
  RealPartDifference < tolerance andalso ImaginaryDifference < tolerance.

exp({complex_number, _RealPart, ImaginaryPart}) ->
  new(math:cos(ImaginaryPart), math:sin(ImaginaryPart)).

imaginary({complex_number, _RealPart, ImaginaryPart}) ->
  ImaginaryPart.

mul(
    {complex_number, RealPart1, ImaginaryPart1},
    {complex_number, RealPart2, ImaginaryPart2}
) ->
  new(
    RealPart1 * RealPart2 - ImaginaryPart1 * ImaginaryPart2,
    RealPart2 * ImaginaryPart1 + RealPart1 * ImaginaryPart2
  ).

new(RealPart, ImaginaryPart) ->
  #complex_number{
    real_part = RealPart,
    imaginary_part = ImaginaryPart
  }.

real({complex_number, RealPart, _ImaginaryPart}) ->
  RealPart.

sub(
    {complex_number, RealPart1, ImaginaryPart1},
    {complex_number, RealPart2, ImaginaryPart2}
) ->
  new(RealPart1 - RealPart2, ImaginaryPart1 - ImaginaryPart2).
