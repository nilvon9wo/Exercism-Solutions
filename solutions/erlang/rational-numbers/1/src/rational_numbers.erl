-module(rational_numbers).

-export([absolute/1, add/2, divide/2, exp/2, mul/2, reduce/1, sub/2]).

-record(rational_number, {numerator, denominator}).

absolute({rational_number, Numerator, Denominator})
  when Numerator < 0 ->
  {rational_number, -Numerator, Denominator};

absolute({rational_number, Numerator, Denominator})
  when Denominator < 0 ->
  {rational_number, Numerator, -Denominator};

absolute({rational_number, _Numerator, _Denominator} = RationalNumber) ->
  RationalNumber;

absolute(R) ->
  RationalNumber = absolute(toRationalNumber(R)),
  fromRationalNumber(RationalNumber).


add(
    {rational_number, Numerator1, Denominator1},
    {rational_number, Numerator2, Denominator2}
) ->
  Sum = toRationalNumber({
    Numerator1 * Denominator2 + Numerator2 * Denominator1,
    Denominator1 * Denominator2
  }),
  reduce(Sum);

add(R1, R2) ->
  RationalNumber = add(toRationalNumber(R1), toRationalNumber(R2)),
  fromRationalNumber(RationalNumber).


divide(
    {rational_number, Numerator1, Denominator1},
    {rational_number, Numerator2, Denominator2}) ->
  Quotient = toRationalNumber({
    Numerator1 * Denominator2,
    Denominator1 * Numerator2
  }),
  reduce(Quotient);

divide(R1, R2) ->
  RationalNumber = divide(toRationalNumber(R1), toRationalNumber(R2)),
  fromRationalNumber(RationalNumber).

exp(Base, {rational_number, Numerator, Denominator}) ->
  math:pow(Base, Numerator / Denominator);

exp(Base, Exponent)
  when is_tuple(Exponent) ->
  RationalNumber = toRationalNumber(Exponent),
  exp(Base, RationalNumber);

exp(
    {rational_number, Numerator, Denominator} = _Base,
    Exponent
) ->
  Result = toRationalNumber({
    round(math:pow(Numerator, Exponent)),
    round(math:pow(Denominator, Exponent))
  }),
  reduce(Result);

exp(Base, Exponent) ->
  RationalNumber = toRationalNumber(Base),
  Result = exp(RationalNumber, Exponent),
  fromRationalNumber(Result).


mul(
    {rational_number, Numerator1, Denominator1},
    {rational_number, Numerator2, Denominator2}) ->
  Product = toRationalNumber({
    Numerator1 * Numerator2,
    Denominator1 * Denominator2
  }),
  reduce(Product);

mul(R1, R2) ->
  RationalNumber = mul(toRationalNumber(R1), toRationalNumber(R2)),
  fromRationalNumber(RationalNumber).

reduce({rational_number, 0, _Denominator}) ->
  {rational_number, 0, 1};

reduce({rational_number, Numerator, Denominator})
  when Numerator < 0
  andalso Denominator < 0 ->
  reduce({rational_number, -Numerator, -Denominator});

reduce({rational_number, Numerator, Denominator} = Rational)
  when Numerator < 0
  orelse Denominator < 0 ->
  AbsoluteRational = absolute(Rational),
  {rational_number, NewNumerator, NewDenominator} = reduce(AbsoluteRational),
  toRationalNumber({-NewNumerator, NewDenominator});

reduce({rational_number, Numerator, Denominator} = Rational) ->
  GreatestCommonDenominator = findGreatestCommonDenominator(Numerator, Denominator),
  HasGreatestCommonDenominator = GreatestCommonDenominator /= nil,
  if
    HasGreatestCommonDenominator ->
      toRationalNumber({
        Numerator div GreatestCommonDenominator,
        Denominator div GreatestCommonDenominator
      });

    true ->
      Rational
  end;

reduce(R) ->
  RationalNumber = reduce(toRationalNumber(R)),
  fromRationalNumber(RationalNumber).

findGreatestCommonDenominator(Numerator, Denominator) ->
  GroupedNumeratorFactors = groupedFactors(Numerator),
  GroupedDenominatorFactors = groupedFactors(Denominator),
  findGreatestCommonFactor(GroupedNumeratorFactors, GroupedDenominatorFactors).


findGreatestCommonFactor(GroupedNumeratorFactors, GroupedDenominatorFactors) ->
  NumeratorFactors = maps:keys(GroupedNumeratorFactors),
  DenominatorFactors = maps:keys(GroupedDenominatorFactors),
  CommonFactors = findCommonFactors(NumeratorFactors, DenominatorFactors),
  LeastMultipliersForCommonFactors = findLeastMultipliersForCommonFactors(
    GroupedNumeratorFactors, GroupedDenominatorFactors, CommonFactors
  ),
  findGreatestCommonFactor(LeastMultipliersForCommonFactors).

findGreatestCommonFactor(LeastMultipliersForCommonFactors) ->
  Factors = maps:keys(LeastMultipliersForCommonFactors),
  LargerFactors = lists:map(
    fun(Factor) ->
      Factor * maps:get(Factor, LeastMultipliersForCommonFactors)
    end,
    Factors
  ),
  Sum = lists:sum(LargerFactors),
  if
    Sum > 0 ->
      Sum;
    true ->
      nil
  end.

findCommonFactors(NumeratorFactors, DenominatorFactors) ->
  lists:filter(
    fun(Factor) ->
      lists:member(Factor, NumeratorFactors)
    end,
    DenominatorFactors
  ).

findLeastMultipliersForCommonFactors(
    GroupedNumeratorFactors, GroupedDenominatorFactors, CommonFactors
) ->
  LeastMultipliersForCommonFactors = lists:map(
    fun(CommonFactor) ->
      NumeratorMultiplier = length(maps:get(CommonFactor, GroupedNumeratorFactors)),
      DenominatorMultiplier = length(maps:get(CommonFactor, GroupedDenominatorFactors)),
      MinimumMultiplier = lists:min([NumeratorMultiplier, DenominatorMultiplier]),
      {CommonFactor, MinimumMultiplier}
    end,
    CommonFactors
  ),
  maps:from_list(LeastMultipliersForCommonFactors).

groupedFactors(Number) ->
  Factors = factors(Number),
  groupBy(
    fun(Factor) ->
      Factor
    end,
    Factors
  ).

sub(
    {rational_number, Numerator1, Denominator1},
    {rational_number, Numerator2, Denominator2}) ->
  Difference = toRationalNumber({
    Numerator1 * Denominator2 - Numerator2 * Denominator1,
    Denominator1 * Denominator2
  }),
  reduce(Difference);

sub(R1, R2) ->
  RationalNumber = sub(toRationalNumber(R1), toRationalNumber(R2)),
  fromRationalNumber(RationalNumber).


toRationalNumber({Numerator, Denominator}) ->
  #rational_number{
    numerator = Numerator,
    denominator = Denominator
  }.

fromRationalNumber({rational_number, Numerator, Denominator}) ->
  {Numerator, Denominator}.

%%  Prime factors %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

factors(Value)
  when Value < 2
  -> [];

factors(Value) ->
  OddPotentialFactors = lists:seq(3, trunc(Value / 2) + 1, 2),
  find_prime_factors([2 | OddPotentialFactors], Value).

find_prime_factors(PotentialFactors, Value) ->
  find_prime_factors(PotentialFactors, Value, _Accumulator = []).

find_prime_factors(PotentialFactors, Value, Accumulator)
  when length(PotentialFactors) == 0 andalso
  length(Accumulator) == 0 ->
  [Value];

find_prime_factors(PotentialFactors, _Value, Accumulator)
  when length(PotentialFactors) == 0 ->
  Accumulator;

find_prime_factors(PotentialFactors, Value, Accumulator) ->
  [Head | Tail] = PotentialFactors,
  if
    (Value rem Head == 0) ->
      find_prime_factors(PotentialFactors, trunc(Value / Head), [Head | Accumulator]);

    true ->
      find_prime_factors(Tail, Value, Accumulator)
  end.

%%  Miscellaneous Helpers %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

groupBy(Function, List) ->
  PointsByRows = lists:foldr(
    fun appendDefinition/2,
    dict:new(),
    [{Function(X), X} || X <- List]
  ),
  Rows = dict:to_list(PointsByRows),
  maps:from_list(Rows).

appendDefinition({Key, Value}, Definition) ->
  dict:append(Key, Value, Definition).


