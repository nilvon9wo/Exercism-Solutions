let square_root n =
  n
  |> float_of_int
  |> Float.sqrt
  |> int_of_float
