let annotate_cell ~(field : Field.t) ~(coordinate : Coordinate.t) : char =
    match Field.cell_at field coordinate with
    | Some '*' -> '*'
    | Some ' ' ->
        let count = Flower_counter.count_adjacent_flowers ~field ~coordinate in
        Cell.to_char_with_count Cell.Empty ~count
    | Some c -> c
    | None -> ' '  (* should not happen *)

let annotate_row ~(field : Field.t) ~(row_index : int) : string =
    let rec collect col_index accumulator =
        if col_index >= Field.width field
            then Base.String.of_char_list (List.rev accumulator)
            else
                let coordinate = Coordinate.create ~row:row_index ~column:col_index in
                let character = annotate_cell ~field ~coordinate in
                collect (col_index + 1) (character :: accumulator)
    in
    collect 0 []

let annotate ~(lines : string list) : string list =
    let field = Field.from_strings lines in
    let rec collect_rows row_index accumulator =
        if row_index >= Field.height field
            then Base.List.rev accumulator
            else
                let row_string = annotate_row ~field ~row_index in
                collect_rows (row_index + 1) (row_string :: accumulator)
    in
    collect_rows 0 []
