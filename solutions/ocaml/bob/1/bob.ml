open Message_tone

let response_for message =
  match classify message with
  | Silence -> "Fine. Be that way!"
  | YellingQuestion -> "Calm down, I know what I'm doing!"
  | Yelling -> "Whoa, chill out!"
  | Question -> "Sure."
  | Statement -> "Whatever."
