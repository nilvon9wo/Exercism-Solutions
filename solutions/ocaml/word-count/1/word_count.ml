open Base

let is_word_character c =
  Char.is_alphanum c
    || Char.equal c '\''

let increment_count = function
     | None -> 1
     | Some number -> number + 1

let add_word counts word =
    Map.update counts word ~f:increment_count

let word_boundary previous current =
    not (is_word_character previous)
        && is_word_character current

let strip_unwanted_characters character =
    Char.equal character '\''
        || not (Char.is_alphanum character)

let clean_token characters =
    characters
    |> String.of_char_list
    |> String.strip ~drop:strip_unwanted_characters

let token_to_option characters =
      let token = clean_token characters in
      if String.is_empty token
        then None
        else Some token

let empty_count_map =
    Map.empty (module String)

let word_count input =
  input
  |> String.lowercase
  |> String.to_list
  |> List.group ~break:word_boundary
  |> List.filter_map ~f:token_to_option
  |> List.fold ~init:empty_count_map ~f:add_word
