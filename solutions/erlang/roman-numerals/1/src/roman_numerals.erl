-module(roman_numerals).

-export([roman/1]).


roman(ArabicNumber) ->
  roman(ArabicNumber, _RomanNumber = "").

roman(0, RomanNumber) ->
  lists:flatten(RomanNumber);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 1000 ->
  roman(ArabicNumber - 1000, [RomanNumber, "M"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 900 ->
  roman(ArabicNumber - 900, [RomanNumber, "CM"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 500 ->
  roman(ArabicNumber - 500, [RomanNumber, "D"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 400 ->
  roman(ArabicNumber - 400, [RomanNumber, "CD"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 100 ->
  roman(ArabicNumber - 100, [RomanNumber, "C"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 90 ->
  roman(ArabicNumber - 90, [RomanNumber, "XC"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 50 ->
  roman(ArabicNumber - 50, [RomanNumber, "L"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 40 ->
  roman(ArabicNumber - 40, [RomanNumber, "XL"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 10 ->
  roman(ArabicNumber - 10, [RomanNumber, "X"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 9 ->
  roman(ArabicNumber - 9, [RomanNumber, "IX"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 5 ->
  roman(ArabicNumber - 5, [RomanNumber, "V"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 4 ->
  roman(ArabicNumber - 4, [RomanNumber, "IV"]);

roman(ArabicNumber, RomanNumber)
  when ArabicNumber >= 1 ->
  roman(ArabicNumber - 1, [RomanNumber, "I"]).