open Ascii_character

let substitute_letter_using_atbash_cipher character =
  if is_lowercase_letter character
    then
        let alphabetical_index = Char.code character - Char.code 'a' in
        Char.chr (Char.code 'z' - alphabetical_index)
    else
        character
