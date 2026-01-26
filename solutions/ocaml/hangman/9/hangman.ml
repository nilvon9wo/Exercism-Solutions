type progress =
      | Win
      | Lose
      | Busy of int

type t = {
      secret_word : string;
      guessed_letters : char list ref;
      remaining_failures : int ref;
      masked_word_signal : string Functional_reactive_programming.signal;
      progress_signal : progress Functional_reactive_programming.signal;
      update_masked_word_signal : string -> unit;
      update_progress_signal : progress -> unit;
}

let convert_internal_progress_to_api (internal_progress: Progress.t) : progress =
      match internal_progress with
      | Progress.Win -> Win
      | Progress.Lose -> Lose
      | Progress.Busy remaining_attempts -> Busy remaining_attempts

let send_progress_to_api (api_game_state : t) (internal_progress : Progress.t) =
      let api_progress = convert_internal_progress_to_api internal_progress in
      api_game_state.update_progress_signal api_progress

let convert_api_game_state_to_internal (api_game_state: t) : Game_state.t = {
        secret_word = api_game_state.secret_word;
        guessed_letters = api_game_state.guessed_letters;
        remaining_failures = api_game_state.remaining_failures;
        update_masked_word_signal = api_game_state.update_masked_word_signal;
        update_progress_signal = send_progress_to_api api_game_state;
}

let create_masked_word_signal ~secret_word ~guessed_letters =
    let initial_masked_word = Logic.generate_masked_word ~secret_word ~guessed_letters in
    Functional_reactive_programming.create_signal initial_masked_word

let create_initial_progress_signal
    ~secret_word
    ~guessed_letters
    ~remaining_failures
    ~(masked_word_reactive : string Functional_reactive_programming.reactive_signal) =
    let game_progress = Logic.evaluate_game_progress ~game_state: {
                                                                   secret_word;
                                                                   guessed_letters;
                                                                   remaining_failures;
                                                                   update_masked_word_signal = masked_word_reactive.update;
                                                                   update_progress_signal = Game_state.dummy_progress_update;
                                                                 }
    in
    let api_progress = convert_internal_progress_to_api game_progress in
    Functional_reactive_programming.create_signal api_progress

let convert_internal_progress_to_api game_progress  =
   match game_progress with
   | Progress.Win -> Win
   | Progress.Lose -> Lose
   | Progress.Busy remaining_attempts -> Busy remaining_attempts

let create secret_word =
  let guessed_letters = ref [] in
  let remaining_failures = ref 9 in
  let masked_word_reactive = create_masked_word_signal ~secret_word ~guessed_letters in
  let progress_reactive = create_initial_progress_signal ~secret_word ~guessed_letters ~remaining_failures ~masked_word_reactive in
  {
        secret_word;
        guessed_letters;
        remaining_failures;
        masked_word_signal = masked_word_reactive.current_value;
        progress_signal = progress_reactive.current_value;
        update_masked_word_signal = masked_word_reactive.update;
        update_progress_signal = progress_reactive.update;
  }

let apply_guess_to_internal_state ~api_game_state ~guessed_letter =
  let internal_game_state = convert_api_game_state_to_internal api_game_state in
  Logic.update_game_state ~game_state:internal_game_state ~guessed_letter

let refresh_masked_word_signal ~api_game_state =
  let updated_masked_word = Logic.generate_masked_word
    ~secret_word:api_game_state.secret_word
    ~guessed_letters:api_game_state.guessed_letters
  in
  api_game_state.update_masked_word_signal updated_masked_word

let refresh_progress_signal ~api_game_state =
  let refreshed_internal_state = convert_api_game_state_to_internal api_game_state in
  let internal_progress = Logic.evaluate_game_progress ~game_state:refreshed_internal_state in
  let api_progress = convert_internal_progress_to_api internal_progress in
  api_game_state.update_progress_signal api_progress

let feed guessed_letter (api_game_state : t) =
  apply_guess_to_internal_state ~api_game_state ~guessed_letter;
  refresh_masked_word_signal ~api_game_state;
  refresh_progress_signal ~api_game_state

let masked_word api_game_state =
    api_game_state.masked_word_signal

let progress api_game_state =
    api_game_state.progress_signal
