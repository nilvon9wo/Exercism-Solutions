open Base

(* ---- Validation helpers ---- *)
let valid_characters = "0123456789 ()+.-"

let contains_letters input =
  String.exists input ~f:Char.is_alpha

let is_invalid_character character =
  not (String.contains valid_characters character)

let contains_invalid_punctuation input =
  String.exists input ~f:is_invalid_character

(* ---- Cleaning ---- *)
let extract_digits input =
  input
  |> String.to_list
  |> List.filter ~f:Char.is_digit
  |> String.of_char_list

(* ---- Length check ---- *)
let drop_country_code digits =
  String.sub digits ~pos:1 ~len:10

let validate_eleven_digit_number digits =
  let first_digit = String.get digits 0 in
  if Char.equal first_digit '1'
  then Ok (drop_country_code digits)
  else Error "11 digits must start with 1"

let validate_length digits =
  match String.length digits with
  | 10 ->
    Ok digits
  | 11 ->
    validate_eleven_digit_number digits
  | length when length < 10 ->
    Error "must not be fewer than 10 digits"
  | _ ->
    Error "must not be greater than 11 digits"

(* ---- Area/Exchange code check ---- *)
let substring_of_digits digits position =
  String.sub digits ~pos:position ~len:3

let code_error code_type code_name =
  Error (code_type ^ " code cannot start with " ^ code_name)

let validate_area_and_exchange_code digits =
  let area_code = substring_of_digits digits 0 in
  let area_code_start = String.get area_code 0 in

  let exchange_code = substring_of_digits digits 3 in
  let exchange_code_start = String.get exchange_code 0 in

  match area_code_start, exchange_code_start with
  | '0', _ -> code_error "area" "zero"
  | '1', _ -> code_error "area" "one"
  | _, '0' -> code_error "exchange" "zero"
  | _, '1' -> code_error "exchange" "one"
  | _ -> Ok digits

(* ---- Main function ---- *)
let clean_and_validate_digits input =
  let digits_only = extract_digits input in
  match validate_length digits_only with
  | Error e ->
    Error e
  | Ok cleaned_digits ->
    validate_area_and_exchange_code cleaned_digits

let number input =
  if contains_letters input
    then Error "letters not permitted"
    else if contains_invalid_punctuation input
        then Error "punctuations not permitted"
        else clean_and_validate_digits input
