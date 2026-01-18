let mask_character secret_word guessed_letters index =
    let current_character = secret_word.[index] in
    if List.mem current_character !guessed_letters
        then current_character
        else '_'

let generate_masked_word ~secret_word ~guessed_letters =
    let word_length = String.length secret_word in
    let mask_function = mask_character secret_word guessed_letters in
    String.init word_length mask_function

let is_letter_guessed guessed_letters character =
    List.mem character !guessed_letters

let evaluate_busy_or_win ~(game_state : Game_state.t) =
    let all_letters_guessed = is_letter_guessed game_state.guessed_letters in
    let secret_word = game_state.secret_word in
    if String.for_all all_letters_guessed secret_word
        then Progress.Win
        else Progress.Busy !(game_state.remaining_failures)

let evaluate_game_progress ~(game_state : Game_state.t) =
    if !(game_state.remaining_failures) < 0
        then Progress.Lose
        else evaluate_busy_or_win ~game_state

let update_game_state ~(game_state : Game_state.t) ~(guessed_letter : char) =
    let guessed_letters = game_state.guessed_letters in
    let letter_already_guessed = List.mem guessed_letter !guessed_letters in
    let is_letter_not_in_word = not (String.contains game_state.secret_word guessed_letter) in
    let should_decrement_failures = letter_already_guessed || is_letter_not_in_word in
    if should_decrement_failures
        then game_state.remaining_failures := !(game_state.remaining_failures) - 1
        else guessed_letters := guessed_letter :: !guessed_letters
