(* message_tone.ml *)
open Base

type message_tone =
  | Silence
  | YellingQuestion
  | Yelling
  | Question
  | Statement

let strip_whitespace message = String.strip message
let contains_letters message =
    message
    |> String.exists ~f:Char.is_alpha

let is_all_uppercase character =
    not (Char.is_alpha character)
        || Char.is_uppercase character

let is_yelling message = contains_letters message
    && String.for_all message ~f:is_all_uppercase

let is_question message = String.is_suffix ~suffix:"?" message

let classify message : message_tone =
  let trimmed_message = strip_whitespace message in
  if String.is_empty trimmed_message
  then Silence
  else
    let yelling = is_yelling trimmed_message in
    let question = is_question trimmed_message in
    match yelling, question with
    | true, true -> YellingQuestion
    | true, false -> Yelling
    | false, true -> Question
    | false, false -> Statement
