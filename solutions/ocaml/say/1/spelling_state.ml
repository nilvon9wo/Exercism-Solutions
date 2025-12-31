type spelling_state = {
  parts : string option list;
  remaining : Int64.t;
}

let initial_state number = {
  parts = [];
  remaining = number;
}
