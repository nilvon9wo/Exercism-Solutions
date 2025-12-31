open Base
open Int64
open Say_joiner

let ones_word_by_values =
  [
    0L, "zero"; 1L, "one"; 2L, "two"; 3L, "three"; 4L, "four";
    5L, "five"; 6L, "six"; 7L, "seven"; 8L, "eight"; 9L, "nine";
    10L, "ten"; 11L, "eleven"; 12L, "twelve"; 13L, "thirteen";
    14L, "fourteen"; 15L, "fifteen"; 16L, "sixteen";
    17L, "seventeen"; 18L, "eighteen"; 19L, "nineteen";
  ]
  |> Map.of_alist_exn (module Int64)

let tens_word_by_values =
  [
    20L, "twenty"; 30L, "thirty"; 40L, "forty";
    50L, "fifty"; 60L, "sixty"; 70L, "seventy";
    80L, "eighty"; 90L, "ninety";
  ]
  |> Map.of_alist_exn (module Int64)

let get_ones_word number =
  Map.find_exn ones_word_by_values number

let get_tens_word tens_value =
  Map.find_exn tens_word_by_values tens_value

let spell_tens_and_ones number =
  let tens_value = (number / 10L) * 10L in
  let ones_value = number % 10L in
  let tens_word = get_tens_word tens_value in
  if ones_value = 0L
      then tens_word
      else join_with_hyphen tens_word (get_ones_word ones_value)

let under_hundred number =
  if number < 20L
      then get_ones_word number
      else spell_tens_and_ones number

let get_hundreds_phrase hundreds =
    join_with_space (get_ones_word hundreds) "hundred"

let under_thousand number =
  let hundreds = number / 100L in
  let remainder = number % 100L in
  match hundreds, remainder with
    | 0L, _ ->
        under_hundred remainder
    | _, 0L ->
        get_hundreds_phrase hundreds
    | _, _ ->
        join_with_space (get_hundreds_phrase hundreds) (under_hundred remainder)

let spell_chunk number =
  if number = 0L then None
  else Some (under_thousand number)
