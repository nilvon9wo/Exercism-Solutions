let rec do_while (body : unit -> unit) (cond : unit -> bool) : unit =
  body ();
  if cond ()
    then do_while body cond