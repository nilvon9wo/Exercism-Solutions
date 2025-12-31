open Base
open Int64
open Scale_descriptor

let min_supported_value = 0L
let max_supported_value = 999_999_999_999L

let is_out_of_supported_range number =
  number < min_supported_value
    || number > max_supported_value

let in_english number =
  if is_out_of_supported_range number
      then Error "input out of range"
      else if number = 0L
          then Ok "zero"
          else spell_large_number number
