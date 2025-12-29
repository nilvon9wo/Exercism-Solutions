open Base

let reverse_string string =
  string
  |> String.to_list
  |> List.rev
  |> String.of_char_list
