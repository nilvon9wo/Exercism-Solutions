open Base

let no_more_bottle_verse =
  "No more bottles of beer on the wall, no more bottles of beer.\n\
   Go to the store and buy some more, 99 bottles of beer on the wall."

let format_verse first_line_fn second_line_fn count =
  let first_line = first_line_fn count in
  let second_line = second_line_fn count in
  first_line ^ "\n" ^ second_line

let recite_verse first_line_fn second_line_fn count =
  if count = 0
  then no_more_bottle_verse
  else format_verse first_line_fn second_line_fn count

let bottle_noun count =
  if count = 1
  then "bottle"
  else "bottles"

let number_with_noun count noun =
    Int.to_string count ^ " " ^ noun

let format_first_line count noun =
  let count_and_noun = number_with_noun count noun in
  count_and_noun ^ " of beer on the wall, " ^ count_and_noun ^ " of beer."

let first_line count =
  let noun = bottle_noun count in
  format_first_line count noun

let format_second_line remaining noun =
  let remaining_and_noun = number_with_noun remaining noun in
  "Take one down and pass it around, " ^ remaining_and_noun ^ " of beer on the wall."

let second_line_with_remaining remaining =
  let noun = bottle_noun remaining in
  format_second_line remaining noun

let second_line count =
  let remaining = count - 1 in
  if remaining > 0
  then second_line_with_remaining remaining
  else "Take it down and pass it around, no more bottles of beer on the wall."

let create_bottle_range start_bottle_count take_down =
  let range_start = start_bottle_count - take_down + 1 in
  List.range ~start:`inclusive ~stop:`inclusive range_start start_bottle_count

let recite start_bottle_count take_down =
  let recite_single count = recite_verse first_line second_line count in
  let bottle_range = create_bottle_range start_bottle_count take_down in

  bottle_range
  |> List.rev_map ~f:recite_single
  |> String.concat ~sep:"\n\n"
