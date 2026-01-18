type t = {
  secret_word : string;
  guessed_letters : char list ref;
  remaining_failures : int ref;
}
