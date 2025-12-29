let two_fer name_option =
  let name = match name_option with
    | Some n -> n
    | None -> "you"
  in
  "One for " ^ name ^ ", one for me."