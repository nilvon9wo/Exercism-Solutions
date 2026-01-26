open React

type progress = Win | Lose | Busy of int

type t = {
  secret_word : string;
  guessed_letters : char list ref;
  remaining_failures : int ref;
  masked_word_signal : string signal;
  progress_signal : progress signal;
  update_masked_word_signal : string -> unit;
  update_progress_signal : progress -> unit;
}

(* Mask the word: show guessed letters, hide others *)
let mask_word_with_guesses secret_word guessed_letters =
  String.init (String.length secret_word) (fun index ->
    let current_letter = secret_word.[index] in
    if List.mem current_letter !guessed_letters then current_letter else '_'
  )

(* Compute game progress *)
let compute_game_progress secret_word guessed_letters remaining_failures =
  if !remaining_failures < 0 then Lose
  else if String.for_all (fun letter -> List.mem letter !guessed_letters) secret_word then Win
  else Busy !remaining_failures

(* Create a new Hangman game *)
let create secret_word =
  let guessed_letters = ref [] in
  let remaining_failures = ref 9 in
  let masked_word_signal, update_masked_word_signal =
    S.create (mask_word_with_guesses secret_word guessed_letters)
  in
  let progress_signal, update_progress_signal =
    S.create (Busy !remaining_failures)
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

(* Feed a letter into the game *)
let feed guessed_letter game_state =
  let letter_already_guessed = List.mem guessed_letter !(game_state.guessed_letters) in
  if letter_already_guessed || not (String.contains game_state.secret_word guessed_letter) then
    game_state.remaining_failures := !(game_state.remaining_failures) - 1
  else
    game_state.guessed_letters := guessed_letter :: !(game_state.guessed_letters);
  game_state.update_masked_word_signal
    (mask_word_with_guesses game_state.secret_word game_state.guessed_letters);
  game_state.update_progress_signal
    (compute_game_progress game_state.secret_word game_state.guessed_letters game_state.remaining_failures)

(* Accessors for signals — must match the MLI *)
let masked_word game_state = game_state.masked_word_signal
let progress game_state = game_state.progress_signal
