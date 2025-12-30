let is_triangle a b c =
  a + b > c
    && a + c > b
    && b + c > a
    && a > 0
    && b > 0
    && c > 0

let is_equilateral a b c =
  is_triangle a b c
    && a = b
    && b = c

let are_all_sides_equal a b c =
    a = b
    || b = c
    || a = c

let is_isosceles a b c =
  is_triangle a b c
    && are_all_sides_equal a b c

let is_scalene a b c =
  is_triangle a b c
    && a <> b
    && b <> c
    && a <> c
