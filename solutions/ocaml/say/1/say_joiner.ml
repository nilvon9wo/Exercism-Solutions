open Base

let join_with_space left right =
  left ^ " " ^ right

let join_with_hyphen left right =
  left ^ "-" ^ right

let join_words words =
    String.concat ~sep:" " words
