let roman_numerals = [
    (1000, "M");
    (900,  "CM");
    (500,  "D");
    (400,  "CD");
    (100,  "C");
    (90,   "XC");
    (50,   "L");
    (40,   "XL");
    (10,   "X");
    (9,    "IX");
    (5,    "V");
    (4,    "IV");
    (1,    "I");
]

let rec build_roman remaining value_symbol_pairs =
    match remaining, value_symbol_pairs with
    | 0, _ ->
        ""
    | _, [] ->
        ""
    | value, (symbol_value, symbol) :: rest ->
        if value >= symbol_value
        then
          let remainder = value - symbol_value in
          symbol ^ build_roman remainder value_symbol_pairs
        else
          build_roman value rest

let to_roman number =
  build_roman number roman_numerals
