let rec do_while (body : unit -> unit) (continue_predicate : unit -> bool) : unit =
  body ();
  if continue_predicate ()
    then do_while body continue_predicate