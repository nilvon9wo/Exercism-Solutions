type best_product_state = {
  mutable product : int option;
  mutable factor_pairs : (int * int) list;
}

let create_new_state () = {
    product = None;
    factor_pairs = []
}

