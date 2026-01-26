open React

type progress = Win | Lose | Busy of int

type t = {
  word : string;
  guessed : char list ref;
  failures_left : int ref;
  masked_word_signal : string signal;
  progress_signal : progress signal;
  set_masked_word : string -> unit;
  set_progress : progress -> unit;
}

(* Mask the word: show guessed letters, hide others *)
let mask_word word guessed =
  String.init (String.length word) (fun i ->
    let c = word.[i] in
    if List.mem c !guessed then c else '_'
  )

(* Compute game progress *)
let compute_progress word guessed failures_left =
  if !failures_left < 0 then Lose
  else if String.for_all (fun c -> List.mem c !guessed) word then Win
  else Busy !failures_left

(* Create a new Hangman game *)
let create word =
  let guessed = ref [] in
  let failures_left = ref 9 in
  let masked_word_signal, set_masked_word = S.create (mask_word word guessed) in
  let progress_signal, set_progress = S.create (Busy !failures_left) in
  {
    word;
    guessed;
    failures_left;
    masked_word_signal;
    progress_signal;
    set_masked_word;
    set_progress;
  }

(* Feed a letter *)
let feed c t =
  let letter_already_guessed = List.mem c !(t.guessed) in
  (* Only decrement failures if the letter is new and wrong, or if repeated *)
  if letter_already_guessed || not (String.contains t.word c) then
    t.failures_left := !(t.failures_left) - 1
  else
    t.guessed := c :: !(t.guessed);
  t.set_masked_word (mask_word t.word t.guessed);
  t.set_progress (compute_progress t.word t.guessed t.failures_left)

(* Accessors for signals *)
let masked_word t = t.masked_word_signal
let progress t = t.progress_signal
