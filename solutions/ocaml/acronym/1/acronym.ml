open Base

let split_into_words phrase =
    phrase
    |> String.split_on_chars ~on:[' '; '-'; '_']

let is_not_empty_string = Fn.non String.is_empty

let get_first_letter word = String.get word 0

let get_first_letter_as_uppercase word =
    word
    |> get_first_letter
    |> Char.uppercase

let first_letters_uppercase words =
  words
  |> List.filter ~f:is_not_empty_string
  |> List.map ~f:get_first_letter_as_uppercase
  |> String.of_char_list

let acronym phrase =
  phrase
  |> split_into_words
  |> first_letters_uppercase
