type t = {
  secret_word : string;
  guessed_letters : char list ref;
  remaining_failures : int ref;
  update_masked_word_signal : string -> unit;
  update_progress_signal : Progress.t -> unit;
}

let dummy_progress_update _ = ()