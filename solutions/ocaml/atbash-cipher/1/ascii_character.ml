let is_lowercase_letter character =
  character >= 'a'
    && character <= 'z'

let is_digit character =
  character >= '0'
    && character <= '9'

let is_alphanumeric character =
  is_lowercase_letter character
    || is_digit character

let to_lowercase character =
  Char.lowercase_ascii character
