let mask_character secret_word guessed_letters index =
    let current_character = secret_word.[index] in
    if List.mem current_character !guessed_letters
        then current_character
        else '_'

let generate_masked_word secret_word guessed_letters =
    let word_length = String.length secret_word in
    let mask_function = mask_character secret_word guessed_letters in
    String.init word_length mask_function

let is_letter_guessed guessed_letters character =
    List.mem character !guessed_letters

let compute_busy_or_win secret_word guessed_letters remaining_failures =
        let all_letters_guessed = is_letter_guessed guessed_letters in
        if String.for_all all_letters_guessed secret_word
            then Progress.Win
            else Busy !remaining_failures

let evaluate_game_progress secret_word guessed_letters remaining_failures =
  if !remaining_failures < 0
    then Progress.Lose
    else compute_busy_or_win secret_word guessed_letters remaining_failures

