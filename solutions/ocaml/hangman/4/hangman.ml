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

let create secret_word =
  let guessed_letters = ref [] in
  let remaining_failures = ref 9 in
  let masked_word_signal, update_masked_word_signal =
    Functional_reactive_programming.create_signal
      (Logic.generate_masked_word secret_word guessed_letters)
  in
  let progress_signal, update_progress_signal =
    Functional_reactive_programming.create_signal
      (let p = Logic.evaluate_game_progress secret_word guessed_letters remaining_failures in
       match p with
       | Progress.Win -> Win
       | Progress.Lose -> Lose
       | Progress.Busy n -> Busy n)
  in
  {
    secret_word;
    guessed_letters;
    remaining_failures;
    masked_word_signal;
    progress_signal;
    update_masked_word_signal;
    update_progress_signal;
  }

let feed guessed_letter game_state =
  let is_letter_already_guessed = List.mem guessed_letter !(game_state.guessed_letters) in
  if is_letter_already_guessed || not (String.contains game_state.secret_word guessed_letter)
        then game_state.remaining_failures := !(game_state.remaining_failures) - 1
        else game_state.guessed_letters := guessed_letter :: !(game_state.guessed_letters);
  game_state.update_masked_word_signal
    (Logic.generate_masked_word game_state.secret_word game_state.guessed_letters);
  game_state.update_progress_signal
    (match Logic.evaluate_game_progress game_state.secret_word game_state.guessed_letters game_state.remaining_failures with
     | Progress.Win -> Win
     | Progress.Lose -> Lose
     | Progress.Busy n -> Busy n)

let masked_word game_state = game_state.masked_word_signal

let progress game_state = game_state.progress_signal
