let flower_count_char garden coordinate =
    let count = Flower_counter.count_adjacent_flowers ~garden ~coordinate in
    Cell.to_char_with_count Cell.Empty ~count

let annotate_cell garden coordinate =
    match Garden.cell_at garden coordinate with
    | Some '*' -> '*'
    | Some ' ' -> flower_count_char garden coordinate
    | Some character -> character
    | None -> ' '  (* unreachable *)

let annotate_at  ~(garden:Garden.t) row column =
    Coordinate.create ~row ~column
    |> annotate_cell garden

let build_row_chars ~(garden:Garden.t) row =
    garden.width
    |> Base.List.init ~f:(annotate_at ~garden row)

let annotate_row garden row =
    row
    |> build_row_chars ~garden
    |> Base.String.of_char_list

let build_rows garden =
    garden.height
    |> Base.List.init ~f:(annotate_row garden)

let annotate ~lines =
    lines
    |> Garden.from_strings
    |> build_rows
