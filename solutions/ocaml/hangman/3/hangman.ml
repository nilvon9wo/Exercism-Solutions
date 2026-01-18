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
  update_masked_word : string -> unit;
  update_progress : progress -> unit;
}

(* Mask the word: show guessed letters, hide others *)
let mask_word word guessed_letters =
  String.init (String.length word) (fun i ->
    let character = word.[i] in
    if List.mem character !guessed_letters then character else '_'
  )

(* Compute current progress *)
let compute_progress secret_word guessed_letters remaining_failures =
  if !remaining_failures < 0 then Lose
  else if String.for_all (fun character -> List.mem character !guessed_letters) secret_word then Win
  else Busy !remaining_failures

(* Create a new Hangman game *)
let create secret_word =
  let guessed_letters = ref [] in
  let remaining_failures = ref 9 in
  let masked_word_signal, update_masked_word = Functional_reactive_programming.create_signal (mask_word secret_word guessed_letters) in
  let progress_signal, update_progress = Functional_reactive_programming.create_signal (Busy !remaining_failures) in
  {
    secret_word;
    guessed_letters;
    remaining_failures;
    masked_word_signal;
    progress_signal;
    update_masked_word;
    update_progress;
  }

(* Feed a letter into the game *)
let feed letter game_state =
  let letter_already_guessed = List.mem letter !(game_state.guessed_letters) in
  (* Decrement remaining failures only if the letter is new and wrong, or repeated *)
  if letter_already_guessed || not (String.contains game_state.secret_word letter) then
    game_state.remaining_failures := !(game_state.remaining_failures) - 1
  else
    game_state.guessed_letters := letter :: !(game_state.guessed_letters);
  game_state.update_masked_word (mask_word game_state.secret_word game_state.guessed_letters);
  game_state.update_progress
    (compute_progress game_state.secret_word game_state.guessed_letters game_state.remaining_failures)

(* Accessor for masked word signal *)
let masked_word game_state = game_state.masked_word_signal

(* Accessor for progress signal *)
let progress game_state = game_state.progress_signal
